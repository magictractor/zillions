/**
 * Copyright 2015-2019 Ken Dobson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.magictractor.zillions.testbed.dynamic;

import static uk.co.magictractor.zillions.testbed.dynamic.DynamicSourceUriUtil.createSourceUri;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.suite.api.ExcludeClassNamePatterns;
import org.junit.platform.suite.api.SelectClasses;
import org.opentest4j.TestAbortedException;

import com.google.common.collect.Iterables;

/**
 * <p>
 * Runs a JUnit suite as dynamic tests without having to change the test engine.
 * Only supports a subset of suite associated annotations. Currently these
 * annotations are supported
 * <ul>
 * <li>@SelectClasses</li>
 * <li>@ExcludeClassNamePatterns</li>
 * </ul>
 * <p>
 * This class will probably be removed once JUnit5 has better support for
 * suites. See https://github.com/junit-team/junit5/issues/744.
 * <ul>
 * <li>TODO! support @SelectPackages</li>
 * <li>TODO! filters should apply to nested suites too</li>
 * <li>TODO! registered extension should apply to nested suites too (see
 * extension in Attic)</li>
 * <li>TODO! fix dynamic test node URI. Requires JUnit enhancement. Have raised
 * https://github.com/junit-team/junit5/issues/1850</li>
 * </ul>
 */
public class DynamicSuite {

    private final Launcher _launcher = LauncherFactory.create();
    private final List<Class<?>> _suiteTestClasses;

    // TODO! threadlocal
    private static final Deque<DynamicSuite> evaluating = new ArrayDeque<>();

    /**
     * Constructor typically used within a method annotated by @TestFactory in a
     * test class which has suite annotations.
     *
     * <pre>
     * &#64;TestFactory
     * public Stream<DynamicNode> suiteFactory() {
     *     return new DynamicSuite(this).stream();
     * }
     * </pre>
     */
    public DynamicSuite(Object suiteTest) {
        this(Collections.singletonList(suiteTest.getClass()));
    }

    public DynamicSuite(Class<?> suiteTestClass) {
        this(Collections.singletonList((Class<?>) suiteTestClass));
    }

    /**
     * @param suiteTestClasses test classes which are annotated
     *        with @SelectClasses
     */
    public DynamicSuite(Class<?>... suiteTestClasses) {
        this(Arrays.asList(suiteTestClasses));
    }

    public DynamicSuite(List<Class<?>> suiteTestClasses) {
        if (suiteTestClasses == null || suiteTestClasses.isEmpty()) {
            throw new IllegalArgumentException("suiteTestClasses must not be null or empty");
        }

        suiteTestClasses.forEach(this::ensureSuiteAnnotations);
        _suiteTestClasses = suiteTestClasses;
    }

    /**
     * Check whether the test class has suite annotations. Currently checks only
     * for @SelectClasses, this is likely to be improved soon.
     */
    private boolean hasSuiteAnnotations(Class<?> testClass) {
        return testClass.getAnnotation(SelectClasses.class) != null;
    }

    private void ensureSuiteAnnotations(Class<?> testClass) {
        if (!hasSuiteAnnotations(testClass)) {
            throw new IllegalArgumentException(
                testClass.getName() + " is used as a suite but does not have a @SelectClasses annotation");
        }
    }

    public Stream<DynamicNode> stream() {
        //        System.err.println("*** stream");
        //        new RuntimeException().printStackTrace(System.err);
        if (evaluating.isEmpty()) {
            System.err.println("*** stream() outer");
            evaluating.push(this);
            Stream<DynamicNode> stream = _suiteTestClasses.stream().map(this::streamForSuite).flatMap(i -> i);
            evaluating.pop();
            return stream;
        }
        else {
            System.err.println("*** stream() inner");
            return Stream.empty();
        }
    }

    private Stream<DynamicNode> streamForSuite(Class<?> suiteClass) {
        List<Class<?>> unfilteredClasses = Arrays.asList(suiteClass.getAnnotation(SelectClasses.class).value());
        SuiteFilters suiteFilters = new SuiteFilters(suiteClass);

        return unfilteredClasses.stream().filter(suiteFilters.getClassPredicate()).map(
            this::streamForUnitTests).flatMap(i -> i);
    }

    private Stream<DynamicNode> streamForUnitTests(Class<?> testClass) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request().selectors(
            DiscoverySelectors.selectClass(testClass)).build();
        TestPlan testPlan = _launcher.discover(request);

        // System.err.println("streamForUnitTests: " + testClass.getSimpleName());

        Set<TestIdentifier> roots = testPlan.getRoots();
        if (roots.size() == 1) {
            // This should always happen, with the engine being the root.
            // Skip the DynamicContainer which shows the engine name.
            TestIdentifier root = Iterables.getOnlyElement(roots);
            roots = testPlan.getChildren(root);
        }
        // else log warning?

        System.err.println("roots: " + roots);
        return streamForTestIdentifiers(testPlan, roots);
    }

    private Stream<DynamicNode> streamForTestIdentifiers(TestPlan testPlan, Set<TestIdentifier> testIdentifiers) {
        System.err.println("streamForTestIdentifiers: " + testIdentifiers);
        return testIdentifiers.stream().map(tid -> createDynamicContainer(testPlan, tid));
    }

    //    private DynamicNode createDynamicNode(TestPlan testPlan, TestIdentifier testIdentifier) {
    //        return testIdentifier.isContainer() ? createDynamicContainer(testPlan, testIdentifier)
    //                : createDynamicTest(testPlan, testIdentifier);
    //    }

    // hmms
    private DynamicNode createDynamicContainer(TestPlan testPlan, TestIdentifier containerIdentifier) {

        if (!containerIdentifier.isContainer()) {
            throw new IllegalStateException("TestIdentifier is not a container");
        }

        TestSource source = containerIdentifier.getSource().get();

        ClassSource classSource = (ClassSource) source;
        LauncherDiscoveryRequest request = requestForClassSource(classSource);
        ClassResultListener classResultListener = new ClassResultListener(containerIdentifier);
        _launcher.execute(request, classResultListener);

        // TODO! _suiteContainer not same as _mostRecentContainer (which is null)??
        return classResultListener._suiteContainerInfo.toDynamicContainer();

        //        Stream<? extends DynamicNode> children = classResultListener._suiteContainer.stream();
        //        return createDynamicContainer(containerIdentifier, children);
    }

    private static final class DynamicContainerInfo {
        private final TestIdentifier _containerIdentifier;
        // null at top
        private DynamicContainerInfo _parentContainerInfo;
        private final List<DynamicNode> _children = new ArrayList<>();

        DynamicContainerInfo(TestIdentifier containerIdentifier) {
            _containerIdentifier = containerIdentifier;
        }

        //public void addTest(DynamicTest dynamicTest) {
        public void addTest(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            System.err.println("addTest: " + testIdentifier + " to " + this);
            DynamicTest dynamicTest = createDynamicNodeForExecutedTestResult(testIdentifier, testExecutionResult);
            _children.add(dynamicTest);
        }

        public DynamicContainerInfo addContainer(TestIdentifier childContainerIdentifier) {
            DynamicContainerInfo childContainerInfo = new DynamicContainerInfo(childContainerIdentifier);
            System.err.println("addContainer: " + childContainerInfo + " to " + this);
            // TODO! assertions
            childContainerInfo._parentContainerInfo = this;
            // ook
            //DynamicContainer container = createDynamicContainerForExecutedTestResults();
            //_children.add(container);
            _children.add(childContainerInfo.toDynamicContainer());
            return childContainerInfo;
        }

        public DynamicContainer toDynamicContainer() {
            // System.err.println("createDynamicContainer: " + containerIdentifier);
            URI uri = createSourceUri(_containerIdentifier);

            String displayName = _containerIdentifier.getDisplayName();
            System.err.println("createDynamicContainer: " + displayName);
            return DynamicContainer.dynamicContainer(displayName, uri, _children.stream());
        }

        /**
         * Test have already been run once to determine the tests in the suite
         * (including dynamic and template tests). To avoid running them again,
         * all tests in the suite are dynamic tests, and they just mimic the
         * original result by either throwing an exception or doing nothing.
         */
        private DynamicTest createDynamicNodeForExecutedTestResult(TestIdentifier testIdentifier,
                TestExecutionResult testExecutionResult) {
            System.err.println("createDynamicNodeForExecutedTestResult " + testIdentifier);

            String displayName = testIdentifier.getDisplayName();
            // TODO! make this conditional (on sys prop? - useful until some gradle bugs fixed)
            if (true) {
                // TODO! gets called multiple times with nested suites
                if (!displayName.contains(".")) {
                    displayName = _containerIdentifier.getDisplayName() + "." + displayName;
                }
            }

            URI testSourceUri = createSourceUri(testIdentifier);
            Executable executable = () -> returnOriginalTestExecutionResult(testExecutionResult);

            return DynamicTest.dynamicTest(displayName, testSourceUri, executable);
        }

        private void returnOriginalTestExecutionResult(TestExecutionResult testExecutionResult) throws Throwable {
            if (testExecutionResult.getThrowable().isPresent()) {
                throw testExecutionResult.getThrowable().get();
            }

            if (!testExecutionResult.getStatus().equals(Status.SUCCESSFUL)) {
                throw new IllegalStateException("Test was not successful, but does not have a Throwable");
            }

            // Do nothing, test will pass.
        }

        //        public Stream<? extends DynamicNode> stream() {
        //            return _children.stream();
        //        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[displayName=" + _containerIdentifier.getDisplayName() + "]";
        }
    }

    private LauncherDiscoveryRequest requestForClassSource(ClassSource classSource) {
        DiscoverySelector selector = DiscoverySelectors.selectClass(classSource.getJavaClass());

        return LauncherDiscoveryRequestBuilder.request().selectors(selector).build();
    }

    private static final class ClassResultListener implements TestExecutionListener {
        // private final TestIdentifier _suiteIdentifier;
        private final DynamicContainerInfo _suiteContainerInfo;
        //private final List<? extends DynamicNode> _children;

        private int _executionDepth;

        private DynamicContainerInfo _mostRecentContainer;
        // private TestExecutionResult _testExecutionResult;

        ClassResultListener(TestIdentifier suiteIdentifier) {
            // _suiteIdentifier = suiteIdentifier;
            _suiteContainerInfo = new DynamicContainerInfo(suiteIdentifier);
            _mostRecentContainer = _suiteContainerInfo;
            //_children = new ArrayList<>();
        }

        //        public Stream<? extends DynamicNode> stream() {
        //            return _suiteContainer.stream();
        //        }

        @Override
        public void executionStarted(TestIdentifier testIdentifier) {
            if (++_executionDepth <= 2) {
                // Skip engine and class name.
                // TODO! could get class name node here and remove other.
                System.out.println("started skip: " + testIdentifier);
                return;
            }
            System.out.println("started: " + testIdentifier);

            checkParentContainer(testIdentifier);

            // System.err.println("++executionStarted: " + testIdentifier);
            /*
             * Use executionStarted() rather than dynamicTestRegistered() for
             * adding containers to include template and parameterised test
             * containers.
             */
            if (testIdentifier.isContainer()) {
                addContainer(testIdentifier);
            }
        }

        @Override
        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            System.out.println("finished: " + testIdentifier);

            // TODO! something like this??
            //            if (--_depth <= 1) {
            //                return;
            //            }

            // TODO! check for anything started but not finished/skipped?
            if (testIdentifier.isContainer()) {
                _executionDepth--;
                // TODO!
                //                if (!testIdentifier.equals(_mostRecentContainer._containerIdentifier)) {
                //                    throw new IllegalStateException(
                //                        "executionFinished() for a contained other than the current container");
                //                }

                // TODO! depth check for this method?
                if (_mostRecentContainer != null) {
                    _mostRecentContainer = _mostRecentContainer._parentContainerInfo;
                }
            }

            if (testIdentifier.isTest()) {
                addTest(testIdentifier, testExecutionResult);
            }
        }

        @Override
        public void executionSkipped(TestIdentifier testIdentifier, String reason) {
            //System.err.println("skipped: " + testIdentifier);
            if (testIdentifier.isTest()) {
                // Test disabled or assumption failed.
                // Dynamic nodes may use assumptions, but cannot be ignored prior to execution.
                // See https://github.com/junit-team/junit5/issues/1439
                addTest(testIdentifier, TestExecutionResult.aborted(new TestAbortedException(reason)));
            }
        }

        private void addTest(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            checkParentContainer(testIdentifier);
            _mostRecentContainer.addTest(testIdentifier, testExecutionResult);
        }

        private void addContainer(TestIdentifier containerIdentifier) {
            checkParentContainer(containerIdentifier);
            _mostRecentContainer = _mostRecentContainer.addContainer(containerIdentifier);
        }

        private void checkParentContainer(TestIdentifier testIdentifier) {
            String parentId = testIdentifier.getParentId().get();
            if (!_mostRecentContainer._containerIdentifier.getUniqueId().equals(parentId)) {
                throw new IllegalStateException("Current container is not a parent of the TestIdentifier");
            }
        }
    }

    /** A holder for filter information for a suite. */
    private static final class SuiteFilters {
        private ClassNameFilter _excludeClassNamePatterns;

        SuiteFilters(Class<?> suiteClass) {
            if (suiteClass.isAnnotationPresent(ExcludeClassNamePatterns.class)) {
                String[] patterns = suiteClass.getAnnotation(ExcludeClassNamePatterns.class).value();
                _excludeClassNamePatterns = ClassNameFilter.excludeClassNamePatterns(patterns);
            }
        }

        Predicate<Class<?>> getClassPredicate() {
            if (_excludeClassNamePatterns != null) {
                // TODO! should this be name or simple name?
                return c -> _excludeClassNamePatterns.apply(c.getName()).included();
            }
            else {
                // No filter.
                return c -> true;
            }
        }
    }

    // TODO! there's probably an existing Java or Guava class which does this
    //    private <T> Stream<T> suppliedStream(Supplier<Stream<T>> streamSupplier) {
    //        return Stream.of(streamSupplier).map(supplier -> supplier.get()).flatMap(i -> i);
    //    }

}

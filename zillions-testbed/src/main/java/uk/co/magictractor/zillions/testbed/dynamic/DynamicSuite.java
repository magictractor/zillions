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

import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
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

    // TODO! threadlocal - if it's useful
    private static final Deque<DynamicSuite> evaluating = new ArrayDeque<>();
    private static final Deque<SuiteRequestBuilder> executing = new ArrayDeque<>();
    private static final Map<MethodSource, DynamicContainer> results = new HashMap<>();

    private final Launcher _launcher = LauncherFactory.create();
    private final List<Class<?>> _suiteTestClasses;

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
        System.err.println("new DynamicSuite: " + suiteTestClasses);

        if (suiteTestClasses == null || suiteTestClasses.isEmpty()) {
            throw new IllegalArgumentException("suiteTestClasses must not be null or empty");
        }

        //suiteTestClasses.forEach(this::ensureSuiteAnnotations);
        _suiteTestClasses = suiteTestClasses;
    }

    /**
     * Check whether the test class has suite annotations. Currently checks only
     * for @SelectClasses, this is likely to be improved soon.
     */
    //    private boolean hasSuiteAnnotations(Class<?> testClass) {
    //        return testClass.getAnnotation(SelectClasses.class) != null;
    //    }

    //    private void ensureSuiteAnnotations(Class<?> testClass) {
    //        if (!hasSuiteAnnotations(testClass)) {
    //            throw new IllegalArgumentException(
    //                testClass.getName() + " is used as a suite but does not have a @SelectClasses annotation");
    //        }
    //    }

    public Stream<DynamicNode> stream() {
        //        System.err.println("*** stream");
        //        new RuntimeException().printStackTrace(System.err);
        System.err.println("stream(): " + this + "  eval " + evaluating.size() + "  exec " + executing.size()); // 00, 01, 02
        if (evaluating.isEmpty() /* && executing.isEmpty() */) {
            System.err.println("*** stream() outer " + this);
            evaluating.push(this);
            Stream<DynamicNode> stream = _suiteTestClasses.stream()
                    .peek(c -> System.err.println("stream() for " + c))
                    .map(this::streamForSuite)
                    .flatMap(i -> i)
                    .peek(i -> System.err.println("result: " + i));
            evaluating.pop();
            return stream;
        }
        else {
            System.err.println("*** stream() inner");
            return Stream.empty();
            //return suppliedStream(this::stream);
        }
    }

    //    private Stream<DynamicNode> streamForSuite(Class<?> suiteClass) {
    //        System.err.println("streamForSuite " + suiteClass.getSimpleName());
    //
    //        List<Class<?>> unfilteredClasses = Arrays.asList(suiteClass.getAnnotation(SelectClasses.class).value());
    //        SuiteFilters suiteFilters = new SuiteFilters(suiteClass);
    //
    //        return unfilteredClasses.stream()
    //                .filter(suiteFilters.getClassPredicate())
    //                .map(this::streamForUnitTests)
    //                .flatMap(i -> i);
    //    }

    private Stream<DynamicNode> streamForSuite(Class<?> suiteClass) {
        //private Stream<DynamicNode> streamForUnitTests(Class<?> suiteClass) {
        //        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
        //                .selectors(DiscoverySelectors.selectClass(testClass))
        //                .build();

        SuiteRequestBuilder requestBuilder = new SuiteRequestBuilder(suiteClass, executing.peek());
        System.err.println("push: " + requestBuilder);
        executing.push(requestBuilder);
        TestPlan testPlan = _launcher.discover(requestBuilder.build());
        //executing.pop();

        // System.err.println("streamForUnitTests: " + testClass.getSimpleName());

        Set<TestIdentifier> testPlanRoots = testPlan.getRoots();
        Set<TestIdentifier> roots = new LinkedHashSet<>();
        if (testPlanRoots.size() == 1) {
            // This should always happen, with the engine being the root.
            // Skip the DynamicContainer which shows the engine name.
            TestIdentifier root = Iterables.getOnlyElement(testPlanRoots);
            roots.addAll(testPlan.getChildren(root));
        }
        else {
            // log warning? throw exception?
            roots.addAll(testPlanRoots);
        }

        System.err.println("roots: " + roots);

        //return streamForTestIdentifiers(testPlan, roots);

        //        return suppliedStream(() -> {
        //            System.err.println("pop: " + executing.peek());
        //            Stream<DynamicNode> stream = streamForTestIdentifiers(testPlan, roots);
        //            executing.pop();
        //            return stream;
        //        });

        return streamForTestIdentifiers(testPlan, roots).onClose(() -> {
            // TODO! no - still closes outer first (e.g. BitSuite before BitShiftSuite)
            SuiteRequestBuilder popped = executing.pop();
            System.err.println("popped: " + popped);
        });
    }

    private Stream<DynamicNode> streamForTestIdentifiers(TestPlan testPlan, Set<TestIdentifier> testIdentifiers) {
        // System.err.println("streamForTestIdentifiers: " + testIdentifiers);
        return testIdentifiers.stream().map(tid -> createNodeForTestIdentifier(testPlan, tid));
    }

    private DynamicNode createNodeForTestIdentifier(TestPlan testPlan, TestIdentifier containerIdentifier) {

        if (!containerIdentifier.isContainer()) {
            throw new IllegalArgumentException("TestIdentifier is not a container");
        }

        TestSource source = containerIdentifier.getSource().get();
        System.err.println("*** Source: " + source);
        MethodSource methodSource = null;
        if (source instanceof MethodSource) {
            methodSource = ((MethodSource) source);
            if (results.containsKey(methodSource)) {
                System.err.println("*** Have seen value before: " + methodSource);
                return results.get(methodSource);
            }
        }

        //        Stream<? extends DynamicNode> children = classResultListener._suiteContainer.stream();
        //        return createDynamicContainer(containerIdentifier, children);

        DynamicContainer container = DynamicContainer.dynamicContainer(containerIdentifier.getDisplayName(), null,
            suppliedStream(() -> executeTests(testPlan, containerIdentifier)));

        if (methodSource != null) {
            results.put(methodSource, container);
            System.err.println("*** Stored result for: " + methodSource);
        }

        return container;
    }

    //    private Class<?> getTestSourceClass(TestIdentifier containerIdentifier) {
    //        TestSource source = containerIdentifier.getSource().get();
    //        if (source instanceof ClassSource) {
    //            return ((ClassSource) source).getJavaClass();
    //        }
    //        throw new IllegalArgumentException("Unable to determine ");
    //    }

    private Stream<? extends DynamicNode> executeTests(TestPlan testPlan, TestIdentifier containerIdentifier) {
        System.err.println("executeTests: " + containerIdentifier + " " + executing.size() + "  " + evaluating.size());

        if (!executing.isEmpty()) {
            // TODO! doc
            // return Stream.empty(); // no tests
            // return suppliedStream(() -> executeTests(testPlan, containerIdentifier)); // infinite loop
        }

        TestSource source = containerIdentifier.getSource().get();

        ClassSource classSource = (ClassSource) source;
        LauncherDiscoveryRequest request = requestForClassSource(classSource);
        ClassResultListener classResultListener = new ClassResultListener(containerIdentifier);
        // executing.push(this);
        _launcher.execute(request, classResultListener);
        // executing.pop();

        // TODO! _suiteContainer not same as _mostRecentContainer
        return classResultListener._suiteContainerInfo.stream();
    }

    private static final class DynamicContainerInfo {
        private final TestIdentifier _containerIdentifier;
        // null at top
        private DynamicContainerInfo _parentContainerInfo;
        private final List<DynamicContainerInfo> _childContainers = new ArrayList<>();
        private final List<DynamicTest> _childTests = new ArrayList<>();
        private TestExecutionResult _problem;

        DynamicContainerInfo(TestIdentifier containerIdentifier) {
            _containerIdentifier = containerIdentifier;
        }

        public void addTest(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            //System.out.println("addTest: " + testIdentifier + " to " + this);
            DynamicTest dynamicTest = createDynamicNodeForTestExecutionResult(testIdentifier, testExecutionResult);
            _childTests.add(dynamicTest);
        }

        public DynamicContainerInfo addContainer(TestIdentifier childContainerIdentifier) {
            DynamicContainerInfo childContainerInfo = new DynamicContainerInfo(childContainerIdentifier);
            //System.out.println("addContainer: " + childContainerInfo + " to " + this);
            // TODO! assertions
            childContainerInfo._parentContainerInfo = this;
            _childContainers.add(childContainerInfo);
            return childContainerInfo;
        }

        private DynamicContainer toDynamicContainer() {

            // System.err.println("createDynamicContainer: " + containerIdentifier);
            URI uri = DynamicSourceUriUtil.createSourceUri(_containerIdentifier);

            String displayName = _containerIdentifier.getDisplayName();
            System.err.println("createDynamicContainer: " + displayName);

            //                    if (_problem != null) {
            //                        return DynamicTest.dynamicTest(displayName, uri, () -> {
            //                            throw new IllegalStateException(_problem);
            //                        });
            //                    }
            return DynamicContainer.dynamicContainer(displayName, uri, stream());
        }

        public Stream<? extends DynamicNode> stream() {
            if (_problem != null) {
                DynamicTest problemNode = createDynamicNodeForTestExecutionResult(_containerIdentifier, _problem);
                return Stream.of(problemNode);
            }

            Stream<DynamicContainer> containerStream = _childContainers.stream()
                    .map(DynamicContainerInfo::toDynamicContainer);
            Stream<DynamicTest> testStream = _childTests.stream();

            return Stream.concat(containerStream, testStream);
        }

        /**
         * Test have already been run once to determine the tests in the suite
         * (including dynamic and template tests). To avoid running them again,
         * all tests in the suite are dynamic tests, and they just mimic the
         * original result by either throwing an exception or doing nothing.
         */
        private DynamicTest createDynamicNodeForTestExecutionResult(TestIdentifier testIdentifier,
                TestExecutionResult testExecutionResult) {

            String displayName = testIdentifier.getDisplayName();
            // TODO! make this conditional (on sys prop? - useful until some gradle bugs fixed)
            if (true) {
                // TODO! gets called multiple times with nested suites
                // if (!displayName.contains(".")) {
                displayName = _containerIdentifier.getDisplayName() + "." + displayName;
                // }
            }

            URI testSourceUri = DynamicSourceUriUtil.createSourceUri(testIdentifier);
            Executable executable = () -> returnOriginalTestExecutionResult(testExecutionResult);

            System.err.println("createDynamicNodeForExecutedTestResult() for " + testIdentifier);

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

        public void setProblem(TestExecutionResult problem) {
            _problem = problem;
        }

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
                // System.out.println("started skip: " + testIdentifier);
                return;
            }
            //System.out.println("started: " + testIdentifier);

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
            System.out.println("finished: " + testIdentifier + "  result " + testExecutionResult);

            // TODO! something like this??
            //            if (--_depth <= 1) {
            //                return;
            //            }

            // TODO! check for anything started but not finished/skipped?
            if (testIdentifier.isContainer()) {
                if (!Status.SUCCESSFUL.equals(testExecutionResult.getStatus())) {
                    _mostRecentContainer.setProblem(testExecutionResult);
                }

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

        // TODO! bin??
        @Override
        public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
            System.err.println("reportingEntryPublished: " + entry);
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
    //    private static final class SuiteFilters {
    //        private ClassNameFilter _excludeClassNamePatterns;
    //
    //        SuiteFilters(Class<?> suiteClass) {
    //            if (suiteClass.isAnnotationPresent(ExcludeClassNamePatterns.class)) {
    //                String[] patterns = suiteClass.getAnnotation(ExcludeClassNamePatterns.class).value();
    //                _excludeClassNamePatterns = ClassNameFilter.excludeClassNamePatterns(patterns);
    //            }
    //        }
    //
    //        Predicate<Class<?>> getClassPredicate() {
    //            if (_excludeClassNamePatterns != null) {
    //                // TODO! should this be name or simple name?
    //                return c -> _excludeClassNamePatterns.apply(c.getName()).included();
    //            }
    //            else {
    //                // No filter.
    //                return c -> true;
    //            }
    //        }
    //    }

    // TODO! there's probably an existing Java or Guava class which does this
    private <T> Stream<T> suppliedStream(Supplier<Stream<T>> streamSupplier) {
        return Stream.of(streamSupplier).map(supplier -> supplier.get()).flatMap(i -> i);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[suiteTestClasses=" + _suiteTestClasses + "]";
    }

}

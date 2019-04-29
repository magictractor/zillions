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
package uk.co.magictractor.zillions.testbed.bigint;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
import org.junit.platform.engine.support.descriptor.ClasspathResourceSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.suite.api.ExcludeClassNamePatterns;
import org.junit.platform.suite.api.SelectClasses;

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
 * <li>TODO! ignored tests are displayed as passed tests (or not displayed),
 * perhaps use assumptions, see
 * https://github.com/junit-team/junit5/issues/1439</li>
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
        return _suiteTestClasses.stream().map(this::streamForSuite).flatMap(i -> i);
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
        return testIdentifiers.stream().map(tid -> createDynamicContainer(testPlan, tid));
    }

    //    private DynamicNode createDynamicNode(TestPlan testPlan, TestIdentifier testIdentifier) {
    //        return testIdentifier.isContainer() ? createDynamicContainer(testPlan, testIdentifier)
    //                : createDynamicTest(testPlan, testIdentifier);
    //    }

    private DynamicNode createDynamicContainer(TestPlan testPlan, TestIdentifier containerIdentifier) {

        if (!containerIdentifier.isContainer()) {
            throw new IllegalStateException("TestIdentifier is not a container");
        }

        TestSource source = containerIdentifier.getSource().get();

        ClassSource classSource = (ClassSource) source;
        LauncherDiscoveryRequest request = requestForClassSource(classSource);
        ClassResultListener classResultListener = new ClassResultListener(containerIdentifier);
        _launcher.execute(request, classResultListener);

        Stream<? extends DynamicNode> children = classResultListener.stream();

        return createDynamicContainer(containerIdentifier, children);
    }

    private DynamicContainer createDynamicContainer(TestIdentifier containerIdentifier,
            Stream<? extends DynamicNode> children) {
        // System.err.println("createDynamicContainer: " + containerIdentifier);
        URI uri = createSourceUri(containerIdentifier);

        return DynamicContainer.dynamicContainer(containerIdentifier.getDisplayName(), uri, children);
    }

    private final class DynamicContainerInfo2 {
        private final TestIdentifier _containerIdentifier;
        // private final DynamicContainer _dynamicContainer;
        // null at top
        private DynamicContainerInfo2 _parentContainerInfo;
        private final List<DynamicNode> _children = new ArrayList<>();

        DynamicContainerInfo2(TestIdentifier containerIdentifier) {
            _containerIdentifier = containerIdentifier;
        }

        //        DynamicContainer createNode() {
        //            System.err.println(
        //                ">> create discovered container: " + _containerIdentifier.getDisplayName() + " " + _children.size());
        //            return createDynamicContainer(_containerIdentifier, _children.stream());
        //        }

        boolean isParent(TestIdentifier nodeIdentifier) {
            String nodeParentId = nodeIdentifier.getParentId().orElse(null);
            String containerId = _containerIdentifier.getUniqueId();

            // System.out.println("nodeParentId: " + nodeParentId);
            // System.out.println("containerId: " + containerId);

            return Objects.equals(nodeParentId, containerId);
        }

        public void addTest(DynamicTest dynamicTest) {
            _children.add(dynamicTest);
        }

        public DynamicContainerInfo2 addContainer(TestIdentifier containerIdentifier) {
            DynamicContainerInfo2 containerInfo = new DynamicContainerInfo2(containerIdentifier);
            // TODO! assertions
            containerInfo._parentContainerInfo = this;
            DynamicContainer container = createDynamicContainerForExecutedTestResults(containerIdentifier,
                containerInfo.stream());
            _children.add(container);
            return containerInfo;
        }

        public Stream<? extends DynamicNode> stream() {
            // TODO Auto-generated method stub
            return _children.stream();
        }

    }
    //    See Stream.generate(Supplier)
    //    private Stream<DynamicNode> streamForDynamicContainerChildren(TestIdentifier testIdentifer) {
    //    }

    private URI createSourceUri(TestIdentifier testIdentifier) {
        if (!testIdentifier.getSource().isPresent()) {
            // Very unlikely, there should always be a source.
            // Just use the default URI.
            return null;
        }

        String className;
        String methodName = null;
        TestSource source = testIdentifier.getSource().get();
        if (source instanceof MethodSource) {
            // The method name cannot currently be included in the source URI.
            // The best we can do is just link back to the class.
            // See https://github.com/junit-team/junit5/issues/1850
            MethodSource methodSource = (MethodSource) source;
            className = methodSource.getClassName();
            methodName = methodSource.getMethodName();
            // TODO! not how the URL will look see the JUnit issue
        }
        else if (source instanceof ClassSource) {
            className = ((ClassSource) source).getClassName();
        }
        else if (source instanceof ClasspathResourceSource) {
            className = ((ClasspathResourceSource) source).getClasspathResourceName();
            // System.err.println("getClasspathResourceName(): " + className);
        }
        else {
            // This should be relaxed to just log a warning.
            throw new IllegalStateException(
                "Code needs modified to create a URI for source type " + source.getClass().getSimpleName());
        }

        return createUri(className, methodName);
    }

    private URI createUri(String className, String methodName) {
        String path = "/" + className.replace('.', '/');
        String query = null;
        if (methodName != null) {
            query = "method=" + methodName;
        }

        URI uri;
        try {
            uri = new URI("classpath", null, path, query, null);
        }
        catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }

        return uri;
    }

    private LauncherDiscoveryRequest requestForClassSource(ClassSource classSource) {
        DiscoverySelector selector = DiscoverySelectors.selectClass(classSource.getJavaClass());

        return LauncherDiscoveryRequestBuilder.request().selectors(selector).build();
    }

    // TODO! static??
    private final class ClassResultListener implements TestExecutionListener {
        // private final TestIdentifier _suiteIdentifier;
        private final DynamicContainerInfo2 _suiteContainer;
        private final List<? extends DynamicNode> _children;

        private int _depth;

        private DynamicContainerInfo2 _mostRecentContainer;
        // private TestExecutionResult _testExecutionResult;

        ClassResultListener(TestIdentifier suiteIdentifier) {
            // _suiteIdentifier = suiteIdentifier;
            _suiteContainer = new DynamicContainerInfo2(suiteIdentifier);
            _mostRecentContainer = _suiteContainer;
            _children = new ArrayList<>();
        }

        public Stream<? extends DynamicNode> stream() {
            return _suiteContainer.stream();
        }

        @Override
        public void executionStarted(TestIdentifier testIdentifier) {
            if (++_depth <= 2) {
                // Skip engine and class name.
                // TODO! could get class name node here and remove other.
                return;
            }

            // System.err.println("++executionStarted: " + testIdentifier);
            /*
             * Use executionStarted() rather than dynamicTestRegistered() for
             * adding containers to include templplate and parameterised test
             * containers.
             */
            if (testIdentifier.isContainer()) {
                addContainer(testIdentifier);
            }
        }

        @Override
        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            _depth--;
            // TODO! not handling CONTAINER_AND_TEST descriptor type properly.
            if (testIdentifier.isTest()) {
                addTest(testIdentifier, testExecutionResult);
            }
        }

        private void addTest(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            DynamicTest dynamicTest = createDynamicNodeForExecutedTestResult(testIdentifier, testExecutionResult);
            updateMostRecentContainer(testIdentifier);
            _mostRecentContainer.addTest(dynamicTest);
        }

        private void addContainer(TestIdentifier containerIdentifier) {
            updateMostRecentContainer(containerIdentifier);
            _mostRecentContainer = _mostRecentContainer.addContainer(containerIdentifier);
        }

        private void updateMostRecentContainer(TestIdentifier nodeIdentifier) {
            // TODO! guard against problems
            while (_mostRecentContainer != null && !_mostRecentContainer.isParent(nodeIdentifier)) {
                _mostRecentContainer = _mostRecentContainer._parentContainerInfo;
            }

            // TODO! this is scruffy
            if (_mostRecentContainer == null) {
                _mostRecentContainer = _suiteContainer;
            }

            // return _mostRecentContainer;
        }
    }

    /**
     * Test have already been run once to determine the tests in the suite
     * (including dynamic and template test). To avoid running them again, all
     * tests in the suite are dynamic tests, and they just mimic the original
     * result.
     */
    private DynamicTest createDynamicNodeForExecutedTestResult(TestIdentifier testIdentifier,
            TestExecutionResult testExecutionResult) {
        String displayName = testIdentifier.getDisplayName();
        URI testSourceUri = createSourceUri(testIdentifier);
        Executable executable = () -> returnOriginalTestExecutionResult(testExecutionResult);

        return DynamicTest.dynamicTest(displayName, testSourceUri, executable);
    }

    private DynamicContainer createDynamicContainerForExecutedTestResults(TestIdentifier testIdentifier,
            Stream<? extends DynamicNode> children) {
        String displayName = testIdentifier.getDisplayName();
        System.err.println("displayName: '" + displayName + "' (" + testIdentifier.getLegacyReportingName() + ")");
        URI testSourceUri = createSourceUri(testIdentifier);

        return DynamicContainer.dynamicContainer(displayName, testSourceUri, children);
    }

    private void returnOriginalTestExecutionResult(TestExecutionResult testExecutionResult) throws Throwable {
        // System.err.println("result: " + testExecutionResult);
        if (testExecutionResult == null) {
            // An ignored test.
            // TODO! is there a way to indicate that the test was ignored?
            // For now, just mark it as a pass.
            return;
        }
        if (testExecutionResult.getThrowable().isPresent()) {
            // System.err.println("rethrow failure");
            throw testExecutionResult.getThrowable().get();
        }
        if (!testExecutionResult.getStatus().equals(Status.SUCCESSFUL)) {
            // System.err.println("missing throwable");
            throw new IllegalStateException("Test was not successful, but does not have a Throwable");
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

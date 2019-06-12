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
package uk.co.magictractor.zillions.testbed.suite;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherFactory;
import org.opentest4j.TestAbortedException;

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
 * <li>TODO! registered extension should apply to nested suites too (see
 * extension in Attic)</li>
 * </ul>
 */
public class DynamicSuite {

    private static final Launcher LAUNCHER = LauncherFactory.create();

    /*
     * Note that tests with multiple threads have had absolutely no testing.
     * This InheritableThreadLocal might not be sufficient.
     */
    private static final InheritableThreadLocal<SharedState> STATE = new InheritableThreadLocal<SharedState>() {
        @Override
        protected SharedState initialValue() {
            return new SharedState();
        }
    };

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

        _suiteTestClasses = suiteTestClasses;
    }

    public Stream<DynamicNode> suiteOfSiblingTestClasses() {
        //return _suiteTestClasses.stream().map(this::streamForSuite).flatMap(i -> i);
        return stream();
    }

    public Stream<DynamicNode> suiteOfSuitesInChildPackages() {
        //return _suiteTestClasses.stream().map(this::streamForSuite).flatMap(i -> i);
        return stream();
    }

    private Stream<DynamicNode> stream() {
        return _suiteTestClasses.stream().map(this::streamForSuite).flatMap(i -> i);
    }

    private Stream<DynamicNode> streamForSuite(Class<?> suiteClass) {

        SuiteRequestBuilder requestBuilder = new SuiteRequestBuilder(suiteClass);

        SharedState state = STATE.get();
        Deque<SuiteRequestBuilder> executing = state._executing;
        SuiteExecutionListener suiteExecutionListener = state._suiteExecutionListener;

        boolean isOuterSuite = executing.isEmpty();
        if (!isOuterSuite) {
            suiteExecutionListener.startInnerSuite();
        }

        executing.push(requestBuilder);
        LAUNCHER.execute(requestBuilder.build(), suiteExecutionListener);
        executing.pop();

        if (isOuterSuite) {
            // Reset state in case there are multiple top-level suites.
            STATE.remove();

            return suiteExecutionListener._topContainer._childContainers.stream()
                    .map(DynamicContainerInfo::toDynamicContainer);
        }
        else {
            return Stream.empty();
        }
    }

    private static final class DynamicContainerInfo {
        private final TestIdentifier _containerIdentifier;
        // null at top
        private DynamicContainerInfo _parentContainerInfo;
        private List<DynamicContainerInfo> _childContainers = new ArrayList<>();
        private final List<DynamicTest> _childTests = new ArrayList<>();
        private TestExecutionResult _problem;

        DynamicContainerInfo(TestIdentifier containerIdentifier) {
            _containerIdentifier = containerIdentifier;
        }

        public void addTest(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            DynamicTest dynamicTest = createDynamicNodeForTestExecutionResult(testIdentifier, testExecutionResult);
            _childTests.add(dynamicTest);
        }

        public DynamicContainerInfo addContainer(TestIdentifier childContainerIdentifier) {
            DynamicContainerInfo childContainerInfo = new DynamicContainerInfo(childContainerIdentifier);
            childContainerInfo._parentContainerInfo = this;
            _childContainers.add(childContainerInfo);
            return childContainerInfo;
        }

        private DynamicContainer toDynamicContainer() {

            String displayName = _containerIdentifier.getDisplayName();
            System.err.println("createDynamicContainer: " + displayName);

            /**
             * <p>
             * TODO! inner "suiteFactory()" method names are not displayed in
             * Eclipse (a container without a name is displayed). Workarounds
             * are modifying the display name (e.g. removing parentheses) or not
             * passing URIs. But not passing the URI for the @TestFactory method
             * alone is not sufficient.
             * </p>
             * <p>
             * Not passing URIs for all dynamic nodes is sufficient - but then
             * the links to test classes are broken.
             * </p>
             * <p>
             * Seems like an Eclipse bug and inner @TestFactory nodes will
             * generally be magicked away, so do no hackery here.
             * <p>
             */
            URI uri = SourceUriSupport.fromTestIdentifier(_containerIdentifier);

            //            if (displayName.endsWith("()")) {
            //                displayName = displayName.substring(0, displayName.length() - 2);
            //            }

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
            // TODO! make this conditional (on sys prop? - useful pending Gradle enhancements)
            if (true) {
                displayName = _containerIdentifier.getDisplayName() + "." + displayName;
            }

            URI testSourceUri = SourceUriSupport.fromTestIdentifier(testIdentifier);
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

        public void setProblem(TestExecutionResult problem) {
            _problem = problem;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[displayName=" + _containerIdentifier.getDisplayName() + "]";
        }

    }

    /** State shared between a test suite and any child suites of that suite. */
    private static final class SharedState {
        private final Deque<SuiteRequestBuilder> _executing = new ArrayDeque<>();
        private final SuiteExecutionListener _suiteExecutionListener = new SuiteExecutionListener();
    }

    private static final class SuiteExecutionListener implements TestExecutionListener {

        private DynamicContainerInfo _topContainer;
        private DynamicContainerInfo _mostRecentContainer;
        private boolean _startInnerSuite;

        @Override
        public void executionStarted(TestIdentifier testIdentifier) {
            System.out.println("executionStarted: " + testIdentifier);

            checkParentContainer(testIdentifier);

            if (testIdentifier.isContainer()) {
                addContainer(testIdentifier);
                if (_startInnerSuite) {
                    /*
                     * _mostRecentContainer is for the engine identifier which
                     * will be removed from the results, its parent will be
                     * the @TestFactory method which calls
                     * DynamicSuite.stream(), which may optionally be removed
                     * too.
                     */

                    DynamicContainerInfo parent;
                    // TODO! can do either of these, make configurable
                    if (2 == 1 + 1) {
                        // Remove inner @TestFactory methods
                        parent = _mostRecentContainer._parentContainerInfo._parentContainerInfo;
                    }
                    else {
                        // Leave node for inner @TestFactory methods
                        parent = _mostRecentContainer._parentContainerInfo;
                        // Share child containers so nodes added to the engine are added to the level above.
                        //_mostRecentContainer._parentContainerInfo._childContainers.remove(_mostRecentContainer);
                        //_mostRecentContainer._childContainers = _mostRecentContainer._parentContainerInfo._childContainers;
                    }

                    // TODO! NOOO!!!! don't clear??
                    parent._childContainers.clear();
                    // Share child containers so nodes added to this container are also added to the parent.
                    _mostRecentContainer._childContainers = parent._childContainers;

                    _startInnerSuite = false;
                }
            }
        }

        @Override
        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            System.out.println("executionFinished: " + testIdentifier + "  result " + testExecutionResult);

            // TODO! check for anything started but not finished/skipped?
            if (testIdentifier.isContainer()) {
                if (!Status.SUCCESSFUL.equals(testExecutionResult.getStatus())) {
                    _mostRecentContainer.setProblem(testExecutionResult);
                }

                // TODO!
                //                if (!testIdentifier.equals(_mostRecentContainer._containerIdentifier)) {
                //                    throw new IllegalStateException(
                //                        "executionFinished() for a contained other than the current container");
                //                }

                _mostRecentContainer = _mostRecentContainer._parentContainerInfo;
            }

            if (testIdentifier.isTest()) {
                addTest(testIdentifier, testExecutionResult);
            }
        }

        @Override
        public void executionSkipped(TestIdentifier testIdentifier, String reason) {
            //System.err.println("skipped: " + testIdentifier);
            if (testIdentifier.isTest()) {
                // @Disabled test disabled or an assumption failed.
                // Dynamic nodes may use assumptions, but cannot be ignored prior to execution.
                // See https://github.com/junit-team/junit5/issues/1439
                addTest(testIdentifier, TestExecutionResult.aborted(new TestAbortedException(reason)));
            }
        }

        // TODO! probably not needed
        @Override
        public void dynamicTestRegistered(TestIdentifier testIdentifier) {
            System.err.println("dynamicTestRegistered: " + testIdentifier);
        }

        public void startInnerSuite() {
            _startInnerSuite = true;
        }

        // TODO! probably not needed
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
            if (_mostRecentContainer == null) {
                if (_topContainer != null) {
                    throw new IllegalStateException("top container has already been set");
                }
                _topContainer = new DynamicContainerInfo(containerIdentifier);
                _mostRecentContainer = _topContainer;
            }
            else {
                _mostRecentContainer = _mostRecentContainer.addContainer(containerIdentifier);
            }
        }

        private void checkParentContainer(TestIdentifier testIdentifier) {
            // TODO! reinstate
            //            String parentId = testIdentifier.getParentId().get();
            //            if (!_mostRecentContainer._containerIdentifier.getUniqueId().equals(parentId)) {
            //                throw new IllegalStateException("Current container is not a parent of the TestIdentifier");
            //            }
        }
    }

    // TODO! there's probably an existing Java or Guava class which does this
    //    private <T> Stream<T> suppliedStream(Supplier<Stream<T>> streamSupplier) {
    //        return Stream.of(streamSupplier).map(supplier -> supplier.get()).flatMap(i -> i);
    //    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[suiteTestClasses=" + _suiteTestClasses + "]";
    }

}

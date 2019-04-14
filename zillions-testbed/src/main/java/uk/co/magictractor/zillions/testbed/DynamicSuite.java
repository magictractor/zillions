package uk.co.magictractor.zillions.testbed;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
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

import junit.framework.AssertionFailedError;

/**
 * Runs a JUnit suite as dynamic tests without having to change the test engine.
 * 
 * Only supports a subset of suite associated annotations. Currently these
 * annotations are supported
 * <ul>
 * <li>@SelectClasses</li>
 * <li>@ExcludeClassNamePatterns</li>
 * </ul>
 * 
 * This class will probably be removed once JUnit5 has better support for
 * suites. See https://github.com/junit-team/junit5/issues/744.
 * 
 * TODO! suite within a suite is broken (inner TestFactoryTestDescriptor does
 * get created)
 * 
 * TODO! ignored tests are displayed as passed tests
 * 
 * TODO! support @SelectPackages
 * 
 * TODO! fix dynamic test node URI. Requires JUnit enhancement. Have raised
 * https://github.com/junit-team/junit5/issues/1850
 */
public class DynamicSuite {

	// static tbc - makes no difference
	private static final Launcher _launcher = LauncherFactory.create();
	private final List<Class<?>> _suiteTestClasses;

	// tbc
	// if it works - use thread local?
	private static TestExecutionListener listener;

	/**
	 * Constructor typically used within a method annotated by @TestFactory in a
	 * test class which has suite annotations.
	 * 
	 * <pre>
	 * &#64;TestFactory
	 * public Stream<DynamicNode> suiteFactory() {
	 * 	return new DynamicSuite(this).stream();
	 * }
	 * </pre>
	 */
	public DynamicSuite(Object suiteTest) {
		this(Collections.singletonList(suiteTest.getClass()));
	}

	/**
	 * @param suiteTestClasses test classes which are annotated with @SelectClasses
	 */
	public DynamicSuite(Class<?>... suiteTestClasses) {
		this(Arrays.asList(suiteTestClasses));
	}

	public DynamicSuite(List<Class<?>> suiteTestClasses) {
		if (suiteTestClasses == null || suiteTestClasses.isEmpty()) {
			throw new IllegalArgumentException("suiteTestClasses must not be null or empty");
		}

		suiteTestClasses.forEach(this::ensureSuiteAnnotations);
		this._suiteTestClasses = suiteTestClasses;
	}

	/**
	 * Check whether the test class has suite annotations.
	 * 
	 * Currently checks only for @SelectClasses, this is likely to be improved soon.
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

//	private List<Class<?>> getSuiteTestClasses(Class<?> suiteClass) {
//		return Arrays.asList(suiteClass.getAnnotation(SelectClasses.class).value());
//	}

	public Stream<DynamicNode> stream() {
		return _suiteTestClasses.stream().map(this::streamForSuite).flatMap(i -> i);
	}

	private Stream<DynamicNode> streamForSuite(Class<?> suiteClass) {
		List<Class<?>> unfilteredClasses = Arrays.asList(suiteClass.getAnnotation(SelectClasses.class).value());
		SuiteFilters suiteFilters = new SuiteFilters(suiteClass);

		return unfilteredClasses.stream().filter(suiteFilters.getClassPredicate()).map(this::streamForUnitTests)
				.flatMap(i -> i);
	}

	private Stream<DynamicNode> streamForUnitTests(Class<?> testClass) {
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
				.selectors(DiscoverySelectors.selectClass(testClass)).build();
		TestPlan testPlan = _launcher.discover(request);

		//System.err.println("streamForUnitTests: " + testClass.getSimpleName());

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
		return testIdentifiers.stream().map(tid -> createDynamicNode(testPlan, tid));
	}

	private DynamicNode createDynamicNode(TestPlan testPlan, TestIdentifier testIdentifier) {
		return testIdentifier.isContainer() ? createDynamicContainer(testPlan, testIdentifier)
				: createDynamicTest(testPlan, testIdentifier);
	}

	private DynamicNode createDynamicContainer(TestPlan testPlan, TestIdentifier containerIdentifier) {

		// TODO! split up

		// todo skip engine root
//		if (!containerIdentifier.getSource().isPresent()) {
//			return DynamicContainer.dynamicContainer(containerIdentifier.getDisplayName(),  Streamm);
//		}

		// TestSource source = containerIdentifier.getSource().get();
		TestSource source = containerIdentifier.getSource().orElse(null);
		Stream<? extends DynamicNode> children;

		if (source == null || source instanceof ClassSource) {

			Set<TestIdentifier> childTids = testPlan.getChildren(containerIdentifier);
			// Stream<? extends DynamicNode> children = childTids.stream().map(tid ->
			// createDynamicNode(testPlan, tid));
			children = suppliedStream(() -> childTids.stream().map(tid -> createDynamicNode(testPlan, tid)));

//children=				Stream.of(childTids).flatMap(tid -> createDynamicNode(testPlan, tid));
		}

		else if (source instanceof MethodSource) {
			// TODO! assert tId has no children?
			MethodSource methodSource = (MethodSource) source;
			children = suppliedStream(() -> streamForContainerChildren(testPlan, containerIdentifier));
			// name should be suiteFactory() - is for first - this doesn't get called for
			// nested suite
			System.err.println("have stream for children of " + methodSource.getMethodName() + "  ::"
					+ containerIdentifier.getDisplayName());
		} else if (source instanceof ClasspathResourceSource) {
			Set<TestIdentifier> childTids = testPlan.getChildren(containerIdentifier);
			System.err.println(">>>>> ClasspathResourceSource has children: " + childTids + "  ::"
					+ containerIdentifier.getDisplayName());
			if (!childTids.isEmpty()) {
				throw new IllegalStateException("Eh? ClasspathResourceSource does have children - yippee!!");
			}
			children = Stream.empty();
		} else {
			// TODO! log warning and ?
			throw new IllegalStateException("Code needs modified to source type " + source.getClass().getSimpleName());
		}

		// temp
		// testPlan.

		return createDynamicContainer(containerIdentifier, children);

		// return null;
	}

	private DynamicContainer createDynamicContainer(TestIdentifier containerIdentifier,
			Stream<? extends DynamicNode> children) {
		// System.err.println("createDynamicContainer: " + containerIdentifier);
		URI uri = createSourceUri(containerIdentifier);

		return DynamicContainer.dynamicContainer(containerIdentifier.getDisplayName(), uri, children);
	}

	private Stream<DynamicNode> xxxstreamForContainerChildren(TestPlan testPlan, TestIdentifier containerIdentifier) {
		MethodSource methodSource = (MethodSource) containerIdentifier.getSource().get();

		// testPlan.
		try {
			return directRunMethod(methodSource);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private <T> T directRunMethod(MethodSource methodSource) throws Exception {
		methodSource.getClassName();
		methodSource.getMethodParameterTypes();

		Class<?> methodClass = Class.forName(methodSource.getClassName());
		Method method = methodClass.getMethod(methodSource.getMethodName());

		// TODO! test class instance and params?
		return (T) method.invoke(null);
	}

	private Stream<? extends DynamicNode> streamForContainerChildren(TestPlan testPlan, TestIdentifier containerIdentifier) {
		MethodSource methodSource = (MethodSource) containerIdentifier.getSource().get();
		System.err.println("evaluating stream for children of " + methodSource.getMethodName());

		Deque<DiscoveredDynamicContainerInfo> containerStack = new ArrayDeque<>();
		containerStack.push(new DiscoveredDynamicContainerInfo(containerIdentifier));

		// List<TestIdentifier> children = new ArrayList<>();
		// DiscoveryListener listener = new DiscoveryListener(children::add);
		// DiscoveryListener listener = new DiscoveryListener(this::discoveredChild);
//		DiscoveryListener listener = new DiscoveryListener((c) -> {
//			boolean isC = containerIdentifier.getUniqueId().equals(c.getParentId());
//			System.err.println("**** container child: " + isC + "  " + c);
//			children.add(c);
//		});
		DiscoveryListener listener = new DiscoveryListener(
				(node) -> handleDiscoveredNode(testPlan, node, containerStack));

		// tbc - likely important
		// ah... can change parent of test descriptor not identifier...
		// children.forEach((c) -> c.

		// tbc - there is still a listener active - OK we need this one, other listener
		// does not see new tests
		executeMethodSource(methodSource, listener);

		// System.err.println("@TestFactory children: " + children);

		// return children.stream().map((tid) -> createDynamicNode(testPlan, tid));
		
		//DiscoveredDynamicContainerInfo first = containerStack.getFirst();
		//System.err.println(first._ch);
		
		//return Stream.of(containerStack.getLast().createNode());
		return containerStack.getLast().createNode().getChildren();
	}

	private void handleDiscoveredNode(TestPlan testPlan, TestIdentifier nodeIdentifier,
			Deque<DiscoveredDynamicContainerInfo> containerStack) {

		if (nodeIdentifier.isContainer()) {
			handleDiscoveredContainer(nodeIdentifier, containerStack);
		} else {
			handleDiscoveredTest(testPlan, nodeIdentifier, containerStack);
		}
	}

	private void handleDiscoveredTest(TestPlan testPlan, TestIdentifier testIdentifier,
			Deque<DiscoveredDynamicContainerInfo> containerStack) {

		DynamicNode dynamicTest = createDynamicTest(testPlan, testIdentifier);

		// TODO! check conatiner at the top of the stack is the test's parent

		DiscoveredDynamicContainerInfo containerInfo = containerStack.peek();

		if (!containerInfo.isParent(testIdentifier)) {
			System.err.println("!!!Cannot find container for dynamic test");
			throw new IllegalStateException("Cannot find container for dynamic test");
		}

		containerInfo._children.add(dynamicTest);
	}

	private void handleDiscoveredContainer(TestIdentifier containerIdentifier,
			Deque<DiscoveredDynamicContainerInfo> containerStack) {

		// TODO!
		while (!containerStack.peek().isParent(containerIdentifier)) {
			System.out.println("pop");
			DiscoveredDynamicContainerInfo popped = containerStack.pop();
			//containerStack.peek()._children.add(popped.createNode());
		}
		System.out.println("done popping");

		DiscoveredDynamicContainerInfo containerInfo = new DiscoveredDynamicContainerInfo(containerIdentifier);
		 containerStack.peek()._children.add(containerInfo.createNode());
		containerStack.push(containerInfo);
	}

	private final class DiscoveredDynamicContainerInfo {
		private final TestIdentifier _containerIdentifier;
		private final List<DynamicNode> _children = new ArrayList<>();

		DiscoveredDynamicContainerInfo(TestIdentifier containerIdentifier) {
			_containerIdentifier = containerIdentifier;
		}

		DynamicContainer createNode() {
			System.err.println(">> create discovered container: " + _containerIdentifier.getDisplayName() + " " + _children.size());
			return createDynamicContainer(_containerIdentifier, _children.stream());
		}

		boolean isParent(TestIdentifier nodeIdentifier) {
			String nodeParentId = nodeIdentifier.getParentId().get();
			String containerId = _containerIdentifier.getUniqueId();
			
			//System.err.println("nodeParentId: " + nodeParentId);
			//System.err.println("containerId:  " + containerId);
			
			return Objects.equals(nodeParentId, containerId);
		}
	}

	// See Stream.generate(Supplier)
//	private Stream<DynamicNode> streamForDynamicContainerChildren(TestIdentifier testIdentifer) {
//		
//	}

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
		} else if (source instanceof ClassSource) {
			className = ((ClassSource) source).getClassName();
		} else if (source instanceof ClasspathResourceSource) {
			className = ((ClasspathResourceSource) source).getClasspathResourceName();
			// System.err.println("getClasspathResourceName(): " + className);
		} else {
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
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}

		return uri;
	}

	private DynamicNode createDynamicTest(TestPlan testPlan, TestIdentifier testIdentifier) {
		URI uri = createSourceUri(testIdentifier);

		System.err.println("createDynamicTest: " + testIdentifier);

		TestSource source = testIdentifier.getSource().get();
//		if (source instanceof ClasspathResourceSource) {
//			// throw new UnsupportedOperationException("what to do with " + source);
//			System.err.println("borked: " + testIdentifier);
//			return dynamicTest("borked", () -> {
//				throw new AssertionFailedError();
//			});
//		}

		return DynamicTest.dynamicTest(testIdentifier.getDisplayName(), uri, () -> executeTest(source, testIdentifier));
	}

	private void executeTest(TestSource testSource, TestIdentifier testIdentifier) throws Throwable {
		
		// TODO! remove - very temporary
		// if (1==1) throw new AssertionFailedError();
		
		// TEMP! tests appearing as siblings rather than children of nested suite
		// classes
		if (testSource instanceof ClasspathResourceSource) {
			System.err.println("skipped execution of " + testSource);
			return;
		}

		MethodSource methodSource = (MethodSource) testSource;

		ResultListener resultListener = new ResultListener(testIdentifier);
		System.err.println("executeTest: " + testIdentifier);

		executeMethodSource(methodSource, resultListener);

		TestExecutionResult testExecutionResult = resultListener._testExecutionResult;
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
		// System.err.println("pass");
	}

	/**
	 * Used for running methods annotated with @TestFactory as well as executing
	 * unit tests encasuplated in dynamic test nodes.
	 */
	private void executeMethodSource(MethodSource methodSource, /*boolean discoverOnly,*/ TestExecutionListener... listeners) {

		// TODO! whole class for unit tests and cache other method results?
		DiscoverySelector selector = DiscoverySelectors.selectMethod(methodSource.getClassName(),
				methodSource.getMethodName());

		// THURSDAY - frig engine id so that test identifiers look right?
		// _testP

		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request().selectors(selector).build();

//		if (discoverOnly) {
			// aah... no listeners for discover()
//		_launcher.discover(request, listeners);
	//	} else {
			_launcher.execute(request, listeners);
	//	}
	}

	private static final class DiscoveryListener implements TestExecutionListener {
		private final Consumer<TestIdentifier> _dynamicTestRegisteredConsumer;

		DiscoveryListener(Consumer<TestIdentifier> dynamicTestRegisteredConsumer) {
			_dynamicTestRegisteredConsumer = dynamicTestRegisteredConsumer;
		}

		public void dynamicTestRegistered(TestIdentifier testIdentifier) {
			System.out.println("**dynamicTestRegistered: " + testIdentifier.getLegacyReportingName() + " " + testIdentifier.getDisplayName());
			_dynamicTestRegisteredConsumer.accept(testIdentifier);
		}

		public void executionStarted(TestIdentifier testIdentifier) {
			//System.err.println("**executionStarted: " + testIdentifier);
		}

		public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
			//System.err.println("**executionFinished: " + testIdentifier);
		}
	}

	private static final class ResultListener implements TestExecutionListener {
		private final TestIdentifier _testIdentifier;
		private TestExecutionResult _testExecutionResult;

		ResultListener(TestIdentifier testIdentifier) {
			_testIdentifier = testIdentifier;
		}

		public void dynamicTestRegistered(TestIdentifier testIdentifier) {
			//System.err.println("++dynamicTestRegistered: " + testIdentifier);
		}

		public void executionStarted(TestIdentifier testIdentifier) {
			//System.err.println("++executionStarted: " + testIdentifier);
		}

		public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
			//System.err.println("++executionFinished: " + testIdentifier);
			if (!testIdentifier.equals(_testIdentifier)) {
				// Also notified when containers finish.
				// System.err.println("ignore other: " + testIdentifier);
				return;
			}
			// System.err.println("finished: " + testExecutionResult);
			_testExecutionResult = testExecutionResult;
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
				return c -> _excludeClassNamePatterns.apply(c.getName()).included();
			} else {
				// No filter.
				return c -> true;
			}
		}
	}

	// TODO! there's probably an existing Java or Guava class which does this
	private <T> Stream<T> suppliedStream(Supplier<Stream<T>> streamSupplier) {
		return Stream.of(streamSupplier).map(supplier -> supplier.get()).flatMap(i -> i);
	}
}

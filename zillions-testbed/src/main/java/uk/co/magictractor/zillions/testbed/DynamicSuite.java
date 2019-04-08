package uk.co.magictractor.zillions.testbed;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
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
 * TODO! suite within a suite is broken
 * 
 * TODO! ignored tests are displayed as passed tests
 * 
 * TODO! support @SelectPackages
 * 
 * TODO! fix dynamic test node URI. Requires JUnit enhancement. Have raised
 * https://github.com/junit-team/junit5/issues/1850
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

		Set<TestIdentifier> roots = testPlan.getRoots();
		if (roots.size() == 1) {
			// This should always happen, with the engine being the root.
			// Skip the DynamicContainer which shows the engine name.
			TestIdentifier root = Iterables.getOnlyElement(roots);
			roots = testPlan.getChildren(root);
		}
		// else log warning?

		return dynamicNodes(testPlan, roots);
	}

	private Stream<DynamicNode> dynamicNodes(TestPlan testPlan, Set<TestIdentifier> testIdentifiers) {
		return testIdentifiers.stream().map(tid -> dynamicNode(testPlan, tid));
	}

	private DynamicNode dynamicNode(TestPlan testPlan, TestIdentifier testIdentifier) {
		return testIdentifier.isContainer() ? dynamicContainer(testPlan, testIdentifier)
				: dynamicTest(testPlan, testIdentifier);
	}

	private DynamicNode dynamicContainer(TestPlan testPlan, TestIdentifier testIdentifier) {
		Stream<? extends DynamicNode> children = testPlan.getChildren(testIdentifier).stream()
				.map(tid -> dynamicNode(testPlan, tid));

		URI uri = getSourceUri(testIdentifier);

		return DynamicContainer.dynamicContainer(testIdentifier.getDisplayName(), uri, children);
	}

	private URI getSourceUri(TestIdentifier testIdentifier) {
		if (!testIdentifier.getSource().isPresent()) {
			// Very unlikely, there should always be a source.
			// Just use the default URI.
			return null;
		}

		String className;
		String methodName = null;
		TestSource source = testIdentifier.getSource().get();
		if (source instanceof ClassSource) {
			className = ((ClassSource) source).getClassName();
		} else if (source instanceof MethodSource) {
			// The method name cannot currently be included in the source URI.
			// The best we can do is just link back to the class.
			// See https://github.com/junit-team/junit5/issues/1850
			MethodSource methodSource = (MethodSource) source;
			className = methodSource.getClassName();
			methodName = methodSource.getMethodName();
		} else {
			// This should be relaxed to just log a warning.
			throw new IllegalStateException(
					"Code needs modified to create a URI for source type " + source.getClass().getSimpleName());
		}

		return getUri(className, methodName);
	}

	private URI getUri(String className, String methodName) {
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

	private DynamicNode dynamicTest(TestPlan testPlan, TestIdentifier testIdentifier) {
		URI uri = getSourceUri(testIdentifier);
		MethodSource source = ((MethodSource) testIdentifier.getSource().get());

		// System.err.println("testIdentifier: " + testIdentifier);
		
		return DynamicTest.dynamicTest(testIdentifier.getDisplayName(), uri, () -> executeTest(source, testIdentifier));
	}

	private void executeTest(TestSource testSource, TestIdentifier testIdentifier) throws Throwable {
		MethodSource methodSource = (MethodSource) testSource;
		
		// TODO! 
		DiscoverySelector selector = DiscoverySelectors.selectMethod(methodSource.getClassName(),
				methodSource.getMethodName());

		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request().selectors(selector).build();

		ResultListener resultListener = new ResultListener(testIdentifier);
		// System.err.println("exec");
		_launcher.execute(request, resultListener);

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

	private static final class ResultListener implements TestExecutionListener {
		private final TestIdentifier _testIdentifier;
		private TestExecutionResult _testExecutionResult;

		ResultListener(TestIdentifier testIdentifier) {
			_testIdentifier=testIdentifier;
		}
		public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
			if (!testIdentifier.equals(_testIdentifier)) {
				// Also notified when containers finish.
				// System.err.println("ignore other: " + testIdentifier);
				return;
			}
			// System.err.println("finished: " +  testExecutionResult);
			_testExecutionResult = testExecutionResult;
		}
	}

}

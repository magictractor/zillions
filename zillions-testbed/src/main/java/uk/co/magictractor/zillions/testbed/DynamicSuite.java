package uk.co.magictractor.zillions.testbed;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.engine.TestSource;
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
import org.junit.platform.suite.api.SelectClasses;

import com.google.common.collect.Iterables;

/**
 * This class will probably be removed once JUnit5 has better support for
 * suites. See https://github.com/junit-team/junit5/issues/744.
 * 
 * TODO! what happens with a suite within a suite?
 * 
 * TODO! check what happens with ignored tests.
 * 
 * TODO! support @SelectPackages and @ExcludeClasses
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

	private List<Class<?>> getSuiteTestClasses(Class<?> testClass) {
		return Arrays.asList(testClass.getAnnotation(SelectClasses.class).value());
	}

	public Stream<DynamicNode> stream() {
		return _suiteTestClasses.stream().map(this::streamForSuite).flatMap(i -> i);
	}

	private Stream<DynamicNode> streamForSuite(Class<?> suiteClass) {
		return getSuiteTestClasses(suiteClass).stream().map(this::streamForUnitTests).flatMap(i -> i);
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

		ClassSource source = ((ClassSource) testIdentifier.getSource().get());
		URI uri = getUri(source.getClassName());

		return DynamicContainer.dynamicContainer(testIdentifier.getDisplayName(), uri, children);
	}

	private URI getUri(String className) {
		// TODO! fix this - was a quick hack
		String p = className.replace('.', '\\');
		Path path = Paths.get(p);
		// System.err.println(path.toUri());

		return path.toUri();
	}

	private DynamicNode dynamicTest(TestPlan testPlan, TestIdentifier testIdentifier) {
		MethodSource source = ((MethodSource) testIdentifier.getSource().get());

		// URI uri = getUri(source.getClassName());
		// URI uri = URI.create(source.getMethodName());
		// URI uri = getUri(source.getClassName() + "." + source.getMethodName());
		// URI uri = getUri(source.getClassName() + ":" + source.getMethodName()); //
		// boom
		// URI uri = URI.create(source.getMethodName() + "()");
		// URI uri = null;
		// URI uri = URI.create("method:" + source.getMethodName());
		// URI uri = URI.create("method:" + source.getMethodName() + "()");

		URI uri = getUri(source.getClassName());
		// uri = new URI(scheme, authority, path, query, fragment)
		try {
			// "file", null, <path>, null, null
			// fragment gives "#<method" - still doesn't work
			// uri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(),
			// source.getMethodName(), uri.getFragment());
			// uri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null,
			// source.getMethodName());
			// uri = new URI(null, null, null, null, source.getMethodName());
			uri = new URI("file", null, uri.getPath() + ".java", "line=23&column=9", null);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		// Aah... use classpath scheme?
		// https://github.com/junit-team/junit5/issues/1467

		System.err.println("test uri: " + uri);

		return DynamicTest.dynamicTest(testIdentifier.getDisplayName(), uri, () -> executeTest(source));
	}

	private void executeTest(TestSource testSource) throws Throwable {
		MethodSource methodSource = (MethodSource) testSource;
		DiscoverySelector selector = DiscoverySelectors.selectMethod(methodSource.getClassName(),
				methodSource.getMethodName());

		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request().selectors(selector).build();

		ResultListener resultListener = new ResultListener();
		_launcher.execute(request, resultListener);

		if (resultListener.throwable != null) {
			throw resultListener.throwable;
		}
	}

	private static final class ResultListener implements TestExecutionListener {
		private Throwable throwable;

		public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
			if (!Status.SUCCESSFUL.equals(testExecutionResult.getStatus())) {
				throwable = testExecutionResult.getThrowable().get();
			}
		}
	}

}

package uk.co.magictractor.zillions.testbed;

import java.util.Random;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

// TODO! This will be deleted once DynamicSuite is stable
public class SimpleDynamicTests {

	private Random randomNumberGenerator = new Random(0L);
	// 0 to 1
	private double chanceOfFailure = 0.25;

	public Stream<DynamicNode> stream() {
		return simpleSuite();
	}

	private Stream<DynamicNode> simpleSuite() {

		DynamicTest test1 = DynamicTest.dynamicTest("test1", this::pass);
		DynamicTest test2 = DynamicTest.dynamicTest("test2", this::fail);

		Stream<DynamicNode> tests = Stream.of(test1, test2);

		DynamicContainer node = DynamicContainer.dynamicContainer("bucket", tests);

		return Stream.of(node);
		// return tests;
	}
	
	private void pass() {
	}

	private void fail() {
		Assertions.fail("dynamic test fail");
	}
}

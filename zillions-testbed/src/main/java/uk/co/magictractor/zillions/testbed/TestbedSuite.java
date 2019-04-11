/**
 * Copyright 2015 Ken Dobson
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
package uk.co.magictractor.zillions.testbed;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.suite.api.SelectClasses;

import uk.co.magictractor.zillions.testbed.arithmetic.AbsTest;
import uk.co.magictractor.zillions.testbed.arithmetic.AddTest;
import uk.co.magictractor.zillions.testbed.arithmetic.MultiplyTest;
import uk.co.magictractor.zillions.testbed.arithmetic.NegateTest;
import uk.co.magictractor.zillions.testbed.arithmetic.SubtractTest;
import uk.co.magictractor.zillions.testbed.bits.AndTest;
import uk.co.magictractor.zillions.testbed.bits.OrTest;
import uk.co.magictractor.zillions.testbed.bits.ShiftLeftTest;
import uk.co.magictractor.zillions.testbed.bits.ShiftRightTest;
import uk.co.magictractor.zillions.testbed.bits.XorTest;
import uk.co.magictractor.zillions.testbed.object.CompareTest;
import uk.co.magictractor.zillions.testbed.object.EqualsTest;
import uk.co.magictractor.zillions.testbed.object.HashCodeTest;
import uk.co.magictractor.zillions.testbed.object.ToStringTest;
import uk.co.magictractor.zillions.testbed.random.RandomTest;

// JUnit5 suite support coming.
// See https://github.com/junit-team/junit5/issues/744.

// TODO! revert to suite of suites
//@SelectClasses({ObjectSuite.class, ArithmeticSuite.class, BitSuite.class})

@SelectClasses({ ToStringTest.class, EqualsTest.class, CompareTest.class, HashCodeTest.class, AddTest.class,
		SubtractTest.class, MultiplyTest.class, NegateTest.class, AbsTest.class, AndTest.class, OrTest.class,
		XorTest.class, ShiftLeftTest.class, ShiftRightTest.class, RandomTest.class })
public abstract class TestbedSuite {

	@TestFactory
	public Stream<DynamicNode> suiteFactory() {
		return new DynamicSuite(this).stream();
		//return simpleSuite();
	}

	@Test
	public void t() {
	}

	private Stream<DynamicNode> simpleSuite() {

		// String p = XorTest.class.getName().replace('.', '\\');
		// Path path = Paths.get(p);
		// URI uri = path.toUri();

		// Class<?> clazz = XorTest.class;
		// URL resource = clazz.getResource(clazz.getSimpleName() + ".class");
		// System.err.println("resource: " + resource);

		// getClass().getProtectionDomain().getCodeSource().getLocation().toURI();

		URI uri;
		try {
			// uri= new URI("file:/uk/co/magictractor/zillions/testbed/bits/XorTest.java");
			// uri= new URI("file:uk/co/magictractor/zillions/testbed/bits/XorTest.java");
			// // boom
			// uri= new URI("classpath:/uk/co/magictractor/zillions/testbed/bits/XorTest");
			// uri = resource.toURI();

			// ah - codeSource() is ...\target\classes - so the dir containing the classes
			// found in
			// https://stackoverflow.com/questions/28673651/how-to-get-the-path-of-src-test-resources-directory-in-junit
			// uri = clazz.getProtectionDomain().getCodeSource().getLocation().toURI();

			// uri = new
			// URI("file:/C:/Users/Ken/git/zillions/zillions-testbed/src/main/java/uk/co/magictractor/zillions/testbed/bits/XorTest.java?line=23&column=12");
			uri = new URI("classpath:/uk/co/magictractor/zillions/testbed/bits/XorTest?line=23&column=12");
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}

		// System.err.println("uri1: " + uri);

		try {
			uri = new URI(uri.getScheme(), uri.getAuthority(), "//"+uri.getRawPath() + ".java", null /*"line=12&column=20"*/, null);
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}

		System.err.println("uri2: " + uri);

		if (!"classpath".equals(uri.getScheme())) {
			debug(Paths.get(uri));
		}

		DynamicTest test1 = DynamicTest.dynamicTest("ook", uri, () -> {
			Assertions.fail("fail");
		});

		Stream<DynamicNode> tests = Stream.of(test1);

		DynamicContainer node = DynamicContainer.dynamicContainer("bucket", uri, tests);

		return Stream.of(node);

		// return tests;
	}

	private void debug(Path path) {
		boolean exists = Files.exists(path);
		System.err.println(exists + "  " + path);

		if (!exists && path.getNameCount() > 0) {
			debug(path.getParent());
		}
	}
}

// See TestFactoryTestDescriptor for converting uri to TestSource

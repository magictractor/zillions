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
package uk.co.magictractor.zillions.testbed;

import java.util.Random;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

// TODO! This will be deleted once DynamicSuite is stable
public class SimpleDynamicTests {

    private Random _randomNumberGenerator = new Random(0L);
    // 0 to 1
    private double _chanceOfFailure = 0.25;

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

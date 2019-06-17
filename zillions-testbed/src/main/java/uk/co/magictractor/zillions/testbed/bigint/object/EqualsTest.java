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
package uk.co.magictractor.zillions.testbed.bigint.object;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.magictractor.zillions.environment.BigIntFactory.from;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.suite.WithinSuite;

@WithinSuite
public class EqualsTest {

    @Test
    public void testEqualsTrue() {
        BigInt bigInt1 = from(10);
        BigInt bigInt2 = from(10);
        assertThat(bigInt1).isEqualTo(bigInt2);
    }

    @Test
    public void testEqualsFalse() {
        BigInt bigInt1 = from(10);
        BigInt bigInt2 = from(11);
        assertThat(bigInt1).isNotEqualTo(bigInt2);
    }

    @Test
    public void testEqualsNull() {
        BigInt bigInt1 = from(10);
        assertThat(bigInt1).isNotEqualTo(null);
    }

    @Test
    public void testEqualsIgnoresLeadingZero() {
        BigInt bigInt1 = from("10");
        BigInt bigInt2 = from("010");
        assertThat(bigInt1).isEqualTo(bigInt2);
    }

    @Test
    public void testEqualsOtherImpl() {
        BigInt bigInt1 = from(0);
        BigInt bigInt2 = new NoOpBigInt();
        // String representations are the same.
        assertThat(bigInt1.toString()).isEqualTo(bigInt1.toString());
        // But equals is false because of the different implementing class.
        assertThat(bigInt1).isNotEqualTo(bigInt2);
    }

}

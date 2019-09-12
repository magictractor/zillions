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

import uk.co.magictractor.jura.suite.WithinSuite;
import uk.co.magictractor.zillions.api.BigInt;

@WithinSuite
public class ToStringTest {
    @Test
    public void testToStringPositive() {
        BigInt bigInt = from("102");
        assertThat(bigInt.toString()).isEqualTo("102");
    }

    @Test
    public void testToStringZero() {
        BigInt bigInt = from("0");
        assertThat(bigInt.toString()).isEqualTo("0");
    }

    @Test
    public void testToStringNegative() {
        BigInt bigInt = from("-98");
        assertThat(bigInt.toString()).isEqualTo("-98");
    }

}

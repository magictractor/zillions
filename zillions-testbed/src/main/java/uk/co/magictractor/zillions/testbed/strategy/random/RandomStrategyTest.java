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
package uk.co.magictractor.zillions.testbed.strategy.random;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.magictractor.zillions.core.BigIntFactory.from;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.core.create.RandomStrategy;
import uk.co.magictractor.zillions.testbed.bigint.AbstractStrategyTest;

public abstract class RandomStrategyTest extends AbstractStrategyTest<RandomStrategy> {

    public RandomStrategyTest() {
        super(RandomStrategy.class);
    }

    public RandomStrategyTest(Class<? extends RandomStrategy> implClass) {
        super(implClass);
    }

    @Test
    public void testRandom() {
        // Lots of bits, so the chances of an intermittent failure are extremely
        // small.
        BigInt random1 = createRandom(1024);
        BigInt random2 = createRandom(1024);

        assertThat(random1).isNotEqualTo(random2);
    }

    private BigInt createRandom(int numBits) {
        BigInt random = getImpl().randomise(BigIntFactory.from(0), numBits);
        assertThat(random.signum()).isGreaterThanOrEqualTo(0);

        return random;
    }

    @Test
    public void testRandomSeed() {

        getImpl().setSeed(1);
        BigInt random1 = createRandom(20);
        getImpl().setSeed(1);
        BigInt random2 = createRandom(20);

        assertThat(random1).isEqualTo(random2);
    }

    @Test
    public void testRandomRange1Bit() {
        checkRandomRangeHiLo(1, 10);
    }

    @Test
    public void testRandomRange2Bit() {
        checkRandomRangeHiLo(2, 30);
    }

    @Test
    public void testRandomRange5Bit() {
        checkRandomRangeHiLo(5, 100);
    }

    @Test
    public void testRandomRange8Bit() {
        checkRandomRangeBitFrequency(8, 100);
    }

    @Test
    public void testRandomRange23Bit() {
        checkRandomRangeBitFrequency(23, 100);
    }

    @Test
    public void testRandomRange24Bit() {
        checkRandomRangeBitFrequency(24, 100);
    }

    /*
     * For use with small numBits where the highest possible random value is almost
     * certain to be encountered.
     */
    private void checkRandomRangeHiLo(int numBits, int repeat) {
        // Use seed for tests to prevent theoretical intermittent test failures.
        getImpl().setSeed(0);

        BigInt lowestActual = createRandom(numBits);
        BigInt highestActual = lowestActual;
        for (int i = 0; i < repeat; i++) {
            BigInt actual = createRandom(numBits);
            if (actual.isLessThan(lowestActual)) {
                lowestActual = actual;
            }
            if (actual.isGreaterThan(highestActual)) {
                highestActual = actual;
            }
        }

        BigInt expectedHighest = from(1).shiftLeft(numBits).subtract(1);

        assertThat(lowestActual).isEqualTo(from(0));
        assertThat(highestActual).isEqualTo(expectedHighest);
    }

    private void checkRandomRangeBitFrequency(int numBits, int repeat) {
        // Use seed for tests to prevent theoretical intermittent test failures.
        getImpl().setSeed(0);

        BigInt allBitsSeen = from(0);
        for (int i = 0; i < repeat; i++) {
            BigInt actual = createRandom(numBits);
            allBitsSeen.or(actual);
        }

        BigInt expectedAllBitsSeen = from(1).shiftLeft(numBits).subtract(1);
        assertThat(allBitsSeen.equals(expectedAllBitsSeen));
    }

}

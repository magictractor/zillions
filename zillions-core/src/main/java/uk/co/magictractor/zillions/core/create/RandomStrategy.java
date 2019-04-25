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
package uk.co.magictractor.zillions.core.create;

import uk.co.magictractor.zillions.core.BigInt;

public interface RandomStrategy {

    /**
     * Set a seed for the random BigInts created by {@link #random}.
     *
     * The same seed will create the same random numbers only within single
     * CreateStrategy implementations.
     *
     * @param seed
     */
    void setSeed(long seed);

    /**
     * <p>
     * Modify and return the given BigInt with a uniformly random value up to the
     * given number of bits
     * <p>
     * Note that the most significant bits may be zero. If creating a test which
     * requires a very large random number, this can be combined with setBit() to
     * ensure that the test doesn't accidentally create a (relatively) small number.
     *
     * @return a uniformly random BigInt in the range 0 to 2^n-1 (inclusive)
     */
    BigInt randomise(BigInt rop, int numBits);

}

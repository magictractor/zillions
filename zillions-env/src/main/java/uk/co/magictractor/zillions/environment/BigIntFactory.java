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
package uk.co.magictractor.zillions.environment;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.api.create.CreateStrategy;
import uk.co.magictractor.zillions.api.importer.BigIntByteImporter;
import uk.co.magictractor.zillions.api.random.RandomStrategy;

public final class BigIntFactory {

    private static final CreateStrategy CREATE = Environment.findImplementation(CreateStrategy.class);
    private static final RandomStrategy RANDOM = Environment.findImplementation(RandomStrategy.class);
    private static final BigIntByteImporter BYTE_IMPORTER = Environment.findImplementation(BigIntByteImporter.class);

    private BigIntFactory() {
    }

    public static BigInt from(String decimal) {
        return CREATE.fromString(decimal);
    }

    public static BigInt from(long value) {
        return CREATE.fromLong(value);
    }

    public static BigInt from(byte[] bytes) {
        return BYTE_IMPORTER.signedFrom(from(0), bytes);
    }

    /**
     * If working with many random numbers, it will be more performant to use
     * {@link RandomStrategy#randomise} to allow memory space occupied by random
     * numbers to be recycled.
     *
     * @return a uniformly random BigInt in the range 0 to 2^n-1 (inclusive)
     */
    public static BigInt random(int numBits) {
        return RANDOM.randomise(from(0), numBits);
    }

}

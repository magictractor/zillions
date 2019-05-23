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
package uk.co.magictractor.zillions.core.random;

import java.util.Random;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.api.random.RandomStrategy;

/**
 * Uses Java's Random class to generate random bytes which is then imported to
 * the BigInt implementation. This allows the same "random" values to be used
 * for benchmarking across all BigInt implementations.
 */
public class JavaRandomStrategy implements RandomStrategy {

    //private static final BigIntByteImporter BYTE_IMPORTER = Environment.findImplementation(BigIntByteImporter.class);

    private final Random _randomNumberGenerator = new Random();

    @Override
    public BigInt randomise(BigInt rop, int numBits) {
        byte[] bytes = generateRandomBytes(numBits);
        return importUnsigned(rop, bytes);
    }

    /**
     * The only use of Environment is in this method so a subclass could
     * override only this method and avoid needing to import zillions-env.
     */
    protected BigInt importUnsigned(BigInt rop, byte[] bytes) {
        // TODO! where should JavaRandomStrategy go in order to allow Environment to be used?
        // return  Environment.findImplementation(BigIntByteImporter.class).unsignedFrom(rop, bytes);
        throw new UnsupportedOperationException("Environment not available - need to move JavaRandomStrategy");
    }

    private byte[] generateRandomBytes(int numBits) {
        int byteCount = (numBits + 7) >> 3;
        byte[] bytes = new byte[byteCount];

        _randomNumberGenerator.nextBytes(bytes);

        // Apply a mask to the most significant byte.
        int keepBits = numBits % 8;
        if (keepBits > 0) {
            int mask = (1 << keepBits) - 1;
            bytes[0] = (byte) (bytes[0] & mask);
        }

        return bytes;
    }

    @Override
    public void setSeed(long seed) {
        _randomNumberGenerator.setSeed(seed);
    }

}

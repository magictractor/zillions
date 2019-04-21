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
package uk.co.magictractor.zillions.core;

import uk.co.magictractor.zillions.core.create.CreateStrategy;
import uk.co.magictractor.zillions.core.create.RandomStrategy;
import uk.co.magictractor.zillions.core.environment.Environment;
import uk.co.magictractor.zillions.core.importer.ByteImporter;

public final class BigIntFactory {

	private static final CreateStrategy CREATE = Environment.getBestAvailableImplementation(CreateStrategy.class);
	private static final RandomStrategy RANDOM = Environment.getBestAvailableImplementation(RandomStrategy.class);
	private static final ByteImporter BYTE_IMPORTER = Environment.getBestAvailableImplementation(ByteImporter.class);

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

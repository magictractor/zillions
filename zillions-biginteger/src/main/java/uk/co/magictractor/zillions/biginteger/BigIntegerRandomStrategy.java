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
package uk.co.magictractor.zillions.biginteger;

import java.math.BigInteger;
import java.util.Random;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.create.RandomStrategy;

public class BigIntegerRandomStrategy implements RandomStrategy {

	private Random _randomNumberGenerator;

	@Override
	public void setSeed(long seed) {
		getRandomNumberGenerator().setSeed(seed);
	}

	@Override
	public BigInt random(int numBits) {
		BigInteger x = new BigInteger(numBits, getRandomNumberGenerator());
		return new BigIntegerBigInt(x);
	}

	private Random getRandomNumberGenerator() {
		if (_randomNumberGenerator == null) {
			_randomNumberGenerator = new Random();
		}
		return _randomNumberGenerator;
	}

}

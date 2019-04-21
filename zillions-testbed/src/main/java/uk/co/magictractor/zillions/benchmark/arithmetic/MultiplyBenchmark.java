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
package uk.co.magictractor.zillions.benchmark.arithmetic;

import static uk.co.magictractor.zillions.core.environment.Environment.getBestAvailableImplementation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.core.create.RandomStrategy;

// hmm - put this in new project which could extend all impls?
public class MultiplyBenchmark {

//	public MultiplyBenchmark() {
//		super(BigInt::multiply);
//	}

	public static void warmUp() {
		getBestAvailableImplementation(RandomStrategy.class).setSeed(0L);
		BigInt x = BigIntFactory.random(0);
		x.multiply(x);
	}
	
	// TODO! standardised random numbers
	@ParameterizedTest
	@ValueSource(ints = { 50000, 100000, 150000, 200000 })
	public void testMultiplyParameterised(int numBits) {
		BigInt x = BigIntFactory.random(numBits-1).setBit(numBits);
		BigInt y = BigIntFactory.random(numBits-1).setBit(numBits);
		long t = recordTime(() -> x.multiply(y));
		
		System.out.println(numBits + " multiplication -> " + t);
	}
	
	private long recordTime(Runnable runnable) {
		long before = System.nanoTime();
		runnable.run();
		long after = System.nanoTime();
		
		return after - before;
	}

}

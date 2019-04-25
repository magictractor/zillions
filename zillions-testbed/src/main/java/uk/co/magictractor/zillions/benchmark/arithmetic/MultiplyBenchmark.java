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
package uk.co.magictractor.zillions.benchmark.arithmetic;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;

// hmm - put this in new project which could extend all impls?
public class MultiplyBenchmark {

    //    public MultiplyBenchmark() {
    //        super(BigInt::multiply);
    //    }

    // helps - but still a hit on the first test - ah! recorded time ok
    // the time reported in JUnit is wonky (can it be altered?)
    @BeforeAll
    public static void warmUp() {
        //getBestAvailableImplementation(RandomStrategy.class).setSeed(0L);
        //BigInt x = BigIntFactory.random(0);
        BigInt x = BigIntFactory.from(0);
        x.multiply(x);
    }

    // TODO! standardised random numbers
    @ParameterizedTest
    // 1 for warming up
    @ValueSource(ints = { 1, 50000, 100000, 150000, 200000 })
    public void testMultiplyParameterised(int numBits) {
        BigInt x = BigIntFactory.random(numBits - 1).setBit(numBits);
        BigInt y = BigIntFactory.random(numBits - 1).setBit(numBits);
        long t = recordTime(() -> x.multiply(y));

        System.out.println(numBits + " multiplication -> " + t / 1.0e9);
    }

    private long recordTime(Runnable runnable) {
        ThreadMXBean mx = ManagementFactory.getThreadMXBean();

        // mx.setThreadCpuTimeEnabled(true);
        System.err.println("isCurrentThreadCpuTimeSupported: " + mx.isCurrentThreadCpuTimeSupported());
        System.err.println("isThreadCpuTimeEnabled: " + mx.isThreadCpuTimeEnabled());

        // long before = mx.getCurrentThreadUserTime();
        // long before = mx.getCurrentThreadCpuTime();
        long before = System.nanoTime();
        runnable.run();
        // long after = mx.getCurrentThreadUserTime();
        // long after = mx.getCurrentThreadCpuTime();
        long after = System.nanoTime();

        return after - before;
        // hmm occassionally 15625000, usually zero (both user time and cpu time)
    }

}

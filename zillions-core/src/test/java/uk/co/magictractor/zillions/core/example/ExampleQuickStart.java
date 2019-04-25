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
package uk.co.magictractor.zillions.core.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.core.NumptyCreateStrategy;
import uk.co.magictractor.zillions.core.junit.TestContextExtension;

// TODO! move this to an examples project
public class ExampleQuickStart {

    @RegisterExtension
    public static TestContextExtension __testContextRule = new TestContextExtension(NumptyCreateStrategy.class);

    @Test
    public void fibonacciWithLong() {
        long a = 1;
        long b = 1;
        int i = 2;
        while (a <= b) {
            long c = a + b;
            a = b;
            b = c;
            i++;
            System.out.println(i + "  " + b);
        }
    }

    @Test
    public void fibonacciWithBigInt() {
        BigInt a = BigIntFactory.from(1);
        BigInt b = BigIntFactory.from(1);
        int i = 2;
        while (i < 100) {
            a.add(b);
            BigInt c = a;
            a = b;
            b = c;
            i++;
            System.out.println(i + "  " + b);
        }
    }

    @Test
    public void factorialWithLong() {
        long f = 1;
        for (int i = 2; i < 24; i++) {
            f = f * i;
            System.out.println(i + "  " + f);
        }
    }

    // Note: f is mutable
    @Test
    public void factorialWithBigInt() {
        BigInt f = BigIntFactory.from(1);
        for (int i = 2; i < 24; i++) {
            f.multiply(i);
            System.out.println(i + "  " + f);
        }
    }

    // TODO! use a Factorial function
    // TODO! show different implementations of the function
    // TODO! show performance differences

}

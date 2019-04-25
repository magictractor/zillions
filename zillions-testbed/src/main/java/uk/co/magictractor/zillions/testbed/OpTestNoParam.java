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
package uk.co.magictractor.zillions.testbed;

import java.util.function.Function;

import org.assertj.core.api.Assertions;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;

public class OpTestNoParam<RESULT> extends OpTest<RESULT> {

    private final Function<BigInt, RESULT> _op;

    protected OpTestNoParam(Class<RESULT> resultClass, Function<BigInt, RESULT> op) {
        super(resultClass);
        _op = op;
    }

    protected void check(long x, long expected) {
        check(BigIntFactory.from(x), resultFrom(expected));
    }

    protected void check(BigInt x, long expected) {
        check(x, resultFrom(expected));
    }

    protected void check(String x, String expected) {
        check(BigIntFactory.from(x), resultFrom(expected));
    }

    protected void check(BigInt x, RESULT expected) {
        RESULT actual = _op.apply(x);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}

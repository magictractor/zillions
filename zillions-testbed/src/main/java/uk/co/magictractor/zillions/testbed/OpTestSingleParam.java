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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;

public abstract class OpTestSingleParam<PARAM, RESULT> extends OpTest<RESULT> {

    private final Class<PARAM> _paramClass;

    private final BiFunction<BigInt, PARAM, RESULT> _op;

    protected OpTestSingleParam(Class<PARAM> paramClass, Class<RESULT> resultClass,
            BiFunction<BigInt, PARAM, RESULT> op) {
        super(resultClass);
        _paramClass = paramClass;
        _op = op;
    }

    protected void check(long x, long y, long expected) {
        check(BigIntFactory.from(x), from(_paramClass, y), resultFrom(expected));
    }

    protected void check(String x, String y, String expected) {
        check(BigIntFactory.from(x), from(_paramClass, y), resultFrom(expected));
    }

    protected void check(long x, long y, boolean expected) {
        check(BigIntFactory.from(x), from(_paramClass, y), resultFrom(expected));
    }

    protected void check(BigInt x, PARAM y, RESULT expected) {
        RESULT actual = _op.apply(x, y);
        assertThat(actual).isEqualTo(expected);
        if (actual instanceof BigInt) {
            // BigInt ops which return a BigInt should always return itself.
            assertThat(actual).isSameAs(x);
        }
    }

}

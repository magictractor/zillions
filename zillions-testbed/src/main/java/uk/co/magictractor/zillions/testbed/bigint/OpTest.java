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
package uk.co.magictractor.zillions.testbed.bigint;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.environment.BigIntFactory;

public abstract class OpTest<RESULT> {

    private final Class<RESULT> _resultClass;

    protected OpTest(Class<RESULT> resultClass) {
        _resultClass = resultClass;
    }

    protected RESULT resultFrom(String value) {
        return from(_resultClass, value);
    }

    protected RESULT resultFrom(long value) {
        return from(_resultClass, value);
    }

    protected RESULT resultFrom(boolean value) {
        return from(_resultClass, value);
    }

    protected <T> T from(Class<T> targetClass, String value) {
        Object result;
        if (BigInt.class.equals(targetClass)) {
            result = BigIntFactory.from(value);
        }
        else {
            throw new IllegalStateException("Code needs modified to convert String to " + targetClass.getSimpleName());
        }
        return (T) result;
    }

    protected <T> T from(Class<T> targetClass, long value) {
        Object result;
        if (BigInt.class.equals(targetClass)) {
            result = BigIntFactory.from(value);
        }
        else if (Long.class.equals(targetClass)) {
            result = value;
        }
        else if (Integer.class.equals(targetClass)) {
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Value out of range for an integer: " + value);
            }
            result = Integer.valueOf((int) value);
        }
        else {
            throw new IllegalStateException("Code needs modified to convert long to " + targetClass.getSimpleName());
        }
        return (T) result;
    }

    protected <T> T from(Class<T> targetClass, boolean value) {
        Object result;
        if (Boolean.class.equals(targetClass)) {
            result = Boolean.valueOf(value);
        }
        else {
            throw new IllegalStateException("Code needs modified to convert boolean to " + targetClass.getSimpleName());
        }
        return (T) result;
    }

}

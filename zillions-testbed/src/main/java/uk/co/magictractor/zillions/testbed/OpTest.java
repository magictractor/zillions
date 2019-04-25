package uk.co.magictractor.zillions.testbed;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;

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
        else if (Integer.class.equals(targetClass)) {
            // TODO! range assertions
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

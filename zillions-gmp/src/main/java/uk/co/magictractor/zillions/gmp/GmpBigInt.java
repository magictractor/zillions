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
package uk.co.magictractor.zillions.gmp;

import static uk.co.magictractor.zillions.gmp.GmpLibInstance.__lib;

import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.gmp.struct.mp_bitcnt_t;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

public class GmpBigInt implements BigInt {

    private mpz_t _mpz = new mpz_t();
    // null unless getAlternateInternalValue() or swap() is called
    private mpz_t _alt;

    public GmpBigInt(String decimal) {
        int ok = __lib.mpz_init_set_str(_mpz, decimal, 10);
        if (ok != 0) {
            throw new IllegalArgumentException("Not a decimal string: " + decimal);
        }
    }

    public GmpBigInt(long x) {
        __lib.mpz_init_set_si(_mpz, x);
    }

    /**
     * Constructor for an initialised mpz_t with unspecified value (is zero).
     *
     * For use by Gmp strategy implementations to create a blank initialised mpz_t.
     */
    public GmpBigInt() {
        __lib.mpz_init(_mpz);
    }

    private GmpBigInt(GmpBigInt other) {
        __lib.mpz_init_set(_mpz, other._mpz);
    }

    @Override
    public BigInt copy() {
        return new GmpBigInt(this);
    }

    @Override
    public BigInt add(BigInt y) {
        __lib.mpz_add(_mpz, _mpz, ((GmpBigInt) y)._mpz);
        return this;
    }

    @Override
    public BigInt add(long y) {
        if (y >= 0) {
            __lib.mpz_add_ui(_mpz, _mpz, y);
        }
        else {
            __lib.mpz_sub_ui(_mpz, _mpz, -y);
        }
        return this;
    }

    @Override
    public BigInt subtract(BigInt y) {
        __lib.mpz_sub(_mpz, _mpz, ((GmpBigInt) y)._mpz);
        return this;
    }

    @Override
    public BigInt subtract(long y) {
        if (y >= 0) {
            __lib.mpz_sub_ui(_mpz, _mpz, y);
        }
        else {
            __lib.mpz_add_ui(_mpz, _mpz, -y);
        }
        return this;
    }

    @Override
    public BigInt multiply(BigInt y) {
        swap();
        __lib.mpz_mul(_mpz, _alt, ((GmpBigInt) y)._mpz);
        return this;
    }

    @Override
    public BigInt multiply(long y) {
        swap();
        __lib.mpz_mul_si(_mpz, _alt, y);
        return this;
    }

    @Override
    public BigInt negate() {
        __lib.mpz_neg(_mpz, _mpz);
        return this;
    }

    @Override
    public BigInt abs() {
        __lib.mpz_abs(_mpz, _mpz);
        return this;
    }

    @Override
    public int signum() {
        return _mpz.mpz_sgn();
    }

    @Override
    public BigInt and(BigInt y) {
        __lib.mpz_and(_mpz, _mpz, ((GmpBigInt) y)._mpz);
        return this;
    }

    @Override
    public BigInt or(BigInt y) {
        __lib.mpz_ior(_mpz, _mpz, ((GmpBigInt) y)._mpz);
        return this;
    }

    @Override
    public BigInt xor(BigInt y) {
        __lib.mpz_xor(_mpz, _mpz, ((GmpBigInt) y)._mpz);
        return this;
    }

    @Override
    public BigInt not() {
        __lib.mpz_com(_mpz, _mpz);
        return this;
    }

    @Override
    public BigInt shiftLeft(int n) {
        if (n > 0) {
            __lib.mpz_mul_2exp(_mpz, _mpz, new mp_bitcnt_t(n));
        }
        else {
            __lib.mpz_fdiv_q_2exp(_mpz, _mpz, new mp_bitcnt_t(-n));
        }
        return this;
    }

    @Override
    public BigInt shiftRight(int n) {
        if (n > 0) {
            __lib.mpz_fdiv_q_2exp(_mpz, _mpz, new mp_bitcnt_t(n));
        }
        else {
            __lib.mpz_mul_2exp(_mpz, _mpz, new mp_bitcnt_t(-n));
        }
        return this;
    }

    @Override
    public BigInt setBit(int n) {
        __lib.mpz_setbit(_mpz, mp_bitcnt_t.forPositiveBitNum(n));
        return this;
    }

    @Override
    public BigInt clearBit(int n) {
        __lib.mpz_clrbit(_mpz, mp_bitcnt_t.forPositiveBitNum(n));
        return this;
    }

    @Override
    public BigInt flipBit(int n) {
        __lib.mpz_combit(_mpz, mp_bitcnt_t.forPositiveBitNum(n));
        return this;
    }

    @Override
    public boolean testBit(int n) {
        // returns 0 or 1
        return __lib.mpz_tstbit(_mpz, mp_bitcnt_t.forPositiveBitNum(n)) != 0;
    }

    @Override
    public int compareTo(BigInt other) {
        return compareTo0((GmpBigInt) other);
    }

    private int compareTo0(GmpBigInt other) {
        return __lib.mpz_cmp(_mpz, other._mpz);
    }

    @Override
    public boolean equals(Object other) {
        boolean equals = false;
        if (other != null && other instanceof GmpBigInt) {
            equals = compareTo0((GmpBigInt) other) == 0;
        }
        return equals;
    }

    @Override
    public int hashCode() {
        long leastSignificantLong = __lib.mpz_get_si(_mpz);
        return (int) (leastSignificantLong ^ (leastSignificantLong >>> 32));
    }

    @Override
    public String toString() {
        return __lib.mpz_get_str(null, 10, _mpz);
    }

    /** For use by Gmp specific strategy implementations. */
    public mpz_t getInternalValue() {
        return _mpz;
    }

    public mpz_t getAlternateInternalValue() {
        if (_alt == null) {
            _alt = new mpz_t();
            __lib.mpz_init(_alt);
        }

        return _alt;
    }

    // TODO! doc
    // See "In-Place Operations" in https://gmplib.org/manual/Efficiency.html
    // Uses a more memory, but more efficient if multiplying a lot (like
    // simple factorial calc)
    private void swap() {
        mpz_t swap = getAlternateInternalValue();
        _alt = _mpz;
        _mpz = swap;
    }

    // TODO! does discard() also belong on the interface?
    public void discard() {
        __lib.mpz_clear(_mpz);
        _mpz = null;

        if (_alt != null) {
            __lib.mpz_clear(_alt);
            _alt = null;
        }
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        discard();
    }

    public static mpz_t getInternalValue(BigInt bigInt) {
        return ((GmpBigInt) bigInt)._mpz;
    }

    public static mpz_t getAltInternalValue(BigInt bigInt) {
        return ((GmpBigInt) bigInt).getAlternateInternalValue();
    }

}

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
package uk.co.magictractor.zillions.gmp.struct;

import com.sun.jna.NativeLong;

/**
 * In gmp.h
 *
 * <pre>
 * typedef  unsigned long int  mp_bitcnt_t;
 * </pre>
 */
public class mp_bitcnt_t extends NativeLong {

    private static final long serialVersionUID = 1L;

    // JNA needs a no-args constructor
    public mp_bitcnt_t() {
        super();
    }

    public mp_bitcnt_t(long bitNum) {
        super(bitNum, true);
    }

    public mp_bitcnt_t forAnyBitNum(long bitNum) {
        return new mp_bitcnt_t(bitNum);
    }

    /** Used with set, clear, flip and test bit methods where negative bit numbers are not permitted.
     *
     * The exception thrown is consistent with BigInteger.
     */
    public static mp_bitcnt_t forPositiveBitNum(long bitNum) {
        if (bitNum < 0) {
            throw new ArithmeticException("Negative bit address");
        }
        return new mp_bitcnt_t(bitNum);
    }

}

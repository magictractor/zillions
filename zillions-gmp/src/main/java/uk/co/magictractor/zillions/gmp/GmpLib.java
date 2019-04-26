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

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import uk.co.magictractor.zillions.gmp.struct.gmp_randstate_t;
import uk.co.magictractor.zillions.gmp.struct.mp_bitcnt_t;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

// https://gmplib.org/manual/Integer-Arithmetic.html
// https://gmplib.org/manual/Integer-Logic-and-Bit-Fiddling.html#Integer-Logic-and-Bit-Fiddling
// https://gmplib.org/manual/Integer-Random-Numbers.html#Integer-Random-Numbers
//
// size_t implemented as int
public interface GmpLib {

    // https://gmplib.org/manual/Initializing-Integers.html#Initializing-Integers
    void mpz_init(mpz_t x);

    // for copy
    void mpz_init_set(mpz_t rop, mpz_t op);

    void mpz_init_set_si(mpz_t rop, long op);

    int mpz_init_set_str(mpz_t rop, String str, int base);

    void mpz_set(mpz_t rop, mpz_t op);

    void mpz_set_si(mpz_t rop, long op);

    void mpz_clear(mpz_t x);

    void mpz_add(mpz_t rop, mpz_t op1, mpz_t op2);

    void mpz_add_ui(mpz_t rop, mpz_t op1, long op2);

    void mpz_sub(mpz_t rop, mpz_t op1, mpz_t op2);

    void mpz_sub_ui(mpz_t rop, mpz_t op1, long op2);

    void mpz_mul(mpz_t rop, mpz_t op1, mpz_t op2);

    void mpz_mul_si(mpz_t rop, mpz_t op1, long op2);

    void mpz_neg(mpz_t rop, mpz_t op);

    void mpz_abs(mpz_t rop, mpz_t op);

    String mpz_get_str(Memory mem, int base, mpz_t op);

    // compare, used for equals()
    int mpz_cmp(mpz_t op1, mpz_t op2);

    // least significant bits, used for hashCode()
    long mpz_get_si(mpz_t op);

    void mpz_and(mpz_t rop, mpz_t op1, mpz_t op2);

    void mpz_ior(mpz_t rop, mpz_t op1, mpz_t op2);

    void mpz_xor(mpz_t rop, mpz_t op1, mpz_t op2);

    // Set rop to the one’s complement of op.
    // Used when importing negative numbers.
    void mpz_com(mpz_t rop, mpz_t op);

    // Set bit bit_index in rop.
    void mpz_setbit(mpz_t rop, mp_bitcnt_t bitIndex);

    // Clear bit bit_index in rop.
    void mpz_clrbit(mpz_t rop, mp_bitcnt_t bitIndex);

    // Complement bit bit_index in rop.
    void mpz_combit(mpz_t rop, mp_bitcnt_t bitIndex);

    // Test bit bit_index in op and return 0 or 1 accordingly.
    int mpz_tstbit(mpz_t op, mp_bitcnt_t bitIndex);

    // Used to find higest set bit (base 2)
    // https://gmplib.org/manual/Miscellaneous-Integer-Functions.html#Miscellaneous-Integer-Functions
    int mpz_sizeinbase(mpz_t op, int base);

    // For bit count
    mp_bitcnt_t mpz_popcount(mpz_t op);

    /**
     * Set rop to op1 times 2 raised to op2. This operation can also be defined as a
     * left shift by op2 bits.
     */
    void mpz_mul_2exp(mpz_t rop, mpz_t op1, mp_bitcnt_t op2);

    /**
     * From https://gmplib.org/manual/Integer-Division.html#Integer-Division
     *
     * For positive n both mpz_fdiv_q_2exp and mpz_tdiv_q_2exp are simple bitwise
     * right shifts. For negative n, mpz_fdiv_q_2exp is effectively an arithmetic
     * right shift treating n as twos complement the same as the bitwise logical
     * functions do, whereas mpz_tdiv_q_2exp effectively treats n as sign and
     * magnitude.
     */
    void mpz_fdiv_q_2exp(mpz_t q, mpz_t n, mp_bitcnt_t b);

    /**
     * Generate a uniformly distributed random integer in the range 0 to 2^n-1,
     * inclusive.
     */
    void mpz_urandomb(mpz_t rop, gmp_randstate_t state, mp_bitcnt_t n);

    /**
     * Initialize state with a default algorithm. This will be a compromise between
     * speed and randomness, and is recommended for applications with no special
     * requirements. Currently this is gmp_randinit_mt.
     */
    // https://gmplib.org/manual/Random-State-Initialization.html#Random-State-Initialization
    void gmp_randinit_default(gmp_randstate_t state);

    // https://gmplib.org/manual/Random-State-Seeding.html#Random-State-Seeding
    void gmp_randseed(gmp_randstate_t state, mpz_t seed);

    // size_t is int
    void mpz_import(mpz_t rop, int count, int order, int size, int endian, int nails, Pointer bytes);

    //void * mpz_export (void *rop, size_t *countp, int order, size_t size, int endian, size_t nails, const mpz_t op)
    Pointer mpz_export(Pointer bytes, IntByReference countp, int order, int size, int endian, int nails, mpz_t op);

    int mpz_get_ui(mpz_t op);

}

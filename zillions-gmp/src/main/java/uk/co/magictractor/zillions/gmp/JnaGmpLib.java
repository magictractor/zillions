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

import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import uk.co.magictractor.zillions.gmp.struct.gmp_randstate_t;
import uk.co.magictractor.zillions.gmp.struct.mp_bitcnt_t;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

public class JnaGmpLib implements GmpLib, Library {

    // passing NativeLibrary.getInstance(libName, options)
    // TODO! properties which are platform specific
    static {
        // Can load before registering.
        // System.load("c:\\gmplib\\libgmp-10.dll");

        // TODO! grubby?
        String libName = Platform.isWindows() ? "libgmp-10" : "gmp";
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(Library.OPTION_FUNCTION_MAPPER, new PrefixFunctionMapper());
        NativeLibrary lib = NativeLibrary.getInstance(libName, options);
        Native.register(lib);
    }

    @Override
    public native void mpz_init(mpz_t x);

    @Override
    public native void mpz_init_set(mpz_t rop, mpz_t op);

    @Override
    public native void mpz_init_set_si(mpz_t rop, NativeLong op);

    @Override
    public native int mpz_init_set_str(mpz_t rop, String str, int base);

    @Override
    public native void mpz_set(mpz_t rop, mpz_t op);

    @Override
    public native void mpz_set_si(mpz_t rop, NativeLong op);

    @Override
    public native void mpz_clear(mpz_t x);

    @Override
    public native void mpz_add(mpz_t rop, mpz_t op1, mpz_t op2);

    @Override
    public native void mpz_add_ui(mpz_t rop, mpz_t op1, NativeLong op2);

    @Override
    public native void mpz_sub(mpz_t rop, mpz_t op1, mpz_t op2);

    @Override
    public native void mpz_sub_ui(mpz_t rop, mpz_t op1, NativeLong op2);

    @Override
    public native void mpz_mul(mpz_t rop, mpz_t op1, mpz_t op2);

    @Override
    public native void mpz_mul_si(mpz_t rop, mpz_t op1, NativeLong op2);

    @Override
    public native void mpz_pow_ui(mpz_t rop, mpz_t base, NativeLong exp);

    @Override
    public native void mpz_neg(mpz_t rop, mpz_t op);

    @Override
    public native void mpz_abs(mpz_t rop, mpz_t op);

    @Override
    public native String mpz_get_str(Memory mem, int base, mpz_t op);

    @Override
    public native int mpz_cmp(mpz_t op1, mpz_t op2);

    @Override
    public native NativeLong mpz_get_si(mpz_t op);

    @Override
    public native void mpz_and(mpz_t rop, mpz_t op1, mpz_t op2);

    @Override
    public native void mpz_ior(mpz_t rop, mpz_t op1, mpz_t op2);

    @Override
    public native void mpz_xor(mpz_t rop, mpz_t op1, mpz_t op2);

    @Override
    public native void mpz_com(mpz_t rop, mpz_t op);

    @Override
    public native int mpz_sizeinbase(mpz_t op, int base);

    @Override
    public native mp_bitcnt_t mpz_popcount(mpz_t op);

    // For shift left
    @Override
    public native void mpz_mul_2exp(mpz_t rop, mpz_t op1, mp_bitcnt_t b);

    // For shift right`
    @Override
    public native void mpz_fdiv_q_2exp(mpz_t q, mpz_t n, mp_bitcnt_t b);

    // Set, clear, flip and test bit.
    @Override
    public native void mpz_setbit(mpz_t rop, mp_bitcnt_t bitIndex);

    @Override
    public native void mpz_clrbit(mpz_t rop, mp_bitcnt_t bitIndex);

    @Override
    public native void mpz_combit(mpz_t rop, mp_bitcnt_t bitIndex);

    @Override
    public native int mpz_tstbit(mpz_t op, mp_bitcnt_t bitIndex);

    @Override
    public native void mpz_import(mpz_t rop, int count, int order, int size, int endian, int nails, Pointer bytes);

    @Override
    public native Pointer mpz_export(Pointer bytes, IntByReference countp, int order, int size, int endian, int nails,
            mpz_t op);

    // Random
    // https://gmplib.org/manual/Integer-Random-Numbers.html#Integer-Random-Numbers
    @Override
    public native void mpz_urandomb(mpz_t rop, gmp_randstate_t state, mp_bitcnt_t n);

    // https://gmplib.org/manual/Random-State-Initialization.html#Random-State-Initialization
    @Override
    public native void gmp_randinit_default(gmp_randstate_t state);

    // https://gmplib.org/manual/Random-State-Seeding.html#Random-State-Seeding
    @Override
    public native void gmp_randseed(gmp_randstate_t state, mpz_t seed);

    @Override
    public native int mpz_get_ui(mpz_t op);

}

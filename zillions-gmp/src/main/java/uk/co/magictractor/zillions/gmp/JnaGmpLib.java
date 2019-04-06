/**
 * Copyright 2015 Ken Dobson
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
import com.sun.jna.Platform;

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

	public native void mpz_init(mpz_t x);

	public native void mpz_init_set_si(mpz_t rop, long op);

	public native int mpz_init_set_str(mpz_t rop, String str, int base);

	public native void mpz_add(mpz_t rop, mpz_t op1, mpz_t op2);

	public native void mpz_add_ui(mpz_t rop, mpz_t op1, long op2);

	public native void mpz_sub(mpz_t rop, mpz_t op1, mpz_t op2);

	public native void mpz_sub_ui(mpz_t rop, mpz_t op1, long op2);

	public native void mpz_mul(mpz_t rop, mpz_t op1, mpz_t op2);

	public native void mpz_mul_si(mpz_t rop, mpz_t op1, long op2);

	public native void mpz_neg(mpz_t rop, mpz_t op);

	public native void mpz_abs(mpz_t rop, mpz_t op);
	
	public native String mpz_get_str(Memory mem, int base, mpz_t op);

	public native int mpz_cmp(mpz_t op1, mpz_t op2);

	public native long mpz_get_si(mpz_t op);

	public native void mpz_and(mpz_t rop, mpz_t op1, mpz_t op2);

	public native void mpz_ior(mpz_t rop, mpz_t op1, mpz_t op2);

	public native void mpz_xor(mpz_t rop, mpz_t op1, mpz_t op2);

}

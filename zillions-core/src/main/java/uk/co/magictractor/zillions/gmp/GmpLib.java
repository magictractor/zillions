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

import com.sun.jna.Memory;

// https://gmplib.org/manual/Integer-Arithmetic.html
// https://gmplib.org/manual/Integer-Logic-and-Bit-Fiddling.html#Integer-Logic-and-Bit-Fiddling
public interface GmpLib {

	void mpz_init(mpz_t x);

	void mpz_init_set_si(mpz_t rop, long op);

	int mpz_init_set_str(mpz_t rop, String str, int base);

	void mpz_add(mpz_t rop, mpz_t op1, mpz_t op2);

	void mpz_add_ui(mpz_t rop, mpz_t op1, long op2);

	void mpz_sub(mpz_t rop, mpz_t op1, mpz_t op2);

	void mpz_sub_ui(mpz_t rop, mpz_t op1, long op2);

	void mpz_mul(mpz_t rop, mpz_t op1, mpz_t op2);

	void mpz_mul_si(mpz_t rop, mpz_t op1, long op2);

	String mpz_get_str(Memory mem, int base, mpz_t op);

	int mpz_cmp(mpz_t op1, mpz_t op2);

	long mpz_get_si(mpz_t op);

	void mpz_and(mpz_t rop, mpz_t op1, mpz_t op2);

	void mpz_ior(mpz_t rop, mpz_t op1, mpz_t op2);

	void mpz_xor(mpz_t rop, mpz_t op1, mpz_t op2);

}
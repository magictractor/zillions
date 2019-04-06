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

import uk.co.magictractor.zillions.core.BigInt;

public class GmpJnaBigInt implements BigInt {

	// TODO! Why not use a static instance? - ah! could have JNA and JNI bindings
	// for the lib.
	// But would not have both at the same same - so static lib should be fine
	private final GmpLib _lib;

	private mpz_t _mpz = new mpz_t();

	public GmpJnaBigInt(String decimal, GmpLib lib) {
		int ok = lib.mpz_init_set_str(_mpz, decimal, 10);
		if (ok != 0) {
			throw new IllegalArgumentException("Not a decimal string: " + decimal);
		}
		_lib = lib;
	}

	public GmpJnaBigInt(long x, GmpLib lib) {
		lib.mpz_init_set_si(_mpz, x);
		_lib = lib;
	}

	public GmpJnaBigInt(BigInt other, GmpLib lib) {
		_lib = lib;
		throw new UnsupportedOperationException("not yet implemented");
	}

	// https://gmplib.org/manual/Initializing-Integers.html#Initializing-Integers
	/*
	 * private GmpJnaBigInt(GmpLib lib) { this.lib = lib; mpz = new mpz_t();
	 * lib.__gmpz_init(mpz); }
	 */

	public BigInt add(BigInt y) {
		_lib.mpz_add(_mpz, _mpz, ((GmpJnaBigInt) y)._mpz);
		return this;
	}

	public BigInt add(long y) {
		if (y >= 0) {
			_lib.mpz_add_ui(_mpz, _mpz, y);
		} else {
			_lib.mpz_sub_ui(_mpz, _mpz, -y);
		}
		return this;
	}

	public BigInt subtract(BigInt y) {
		_lib.mpz_sub(_mpz, _mpz, ((GmpJnaBigInt) y)._mpz);
		return this;
	}

	public BigInt subtract(long y) {
		if (y >= 0) {
			_lib.mpz_sub_ui(_mpz, _mpz, y);
		} else {
			_lib.mpz_add_ui(_mpz, _mpz, -y);
		}
		return this;
	}

	public BigInt multiply(BigInt y) {
		_lib.mpz_mul(_mpz, _mpz, ((GmpJnaBigInt) y)._mpz);
		return this;
	}

	public BigInt multiply(long y) {
		_lib.mpz_mul_si(_mpz, _mpz, y);
		return this;
	}

	public BigInt and(BigInt y) {
		_lib.mpz_and(_mpz, _mpz, ((GmpJnaBigInt) y)._mpz);
		return this;
	}

	public BigInt or(BigInt y) {
		_lib.mpz_ior(_mpz, _mpz, ((GmpJnaBigInt) y)._mpz);
		return this;
	}

	public BigInt xor(BigInt y) {
		_lib.mpz_xor(_mpz, _mpz, ((GmpJnaBigInt) y)._mpz);
		return this;
	}

	public boolean equals(Object other) {
		boolean equals = false;
		if (other != null && other instanceof GmpJnaBigInt) {
			equals = (_lib.mpz_cmp(_mpz, ((GmpJnaBigInt) other)._mpz) == 0);
		}
		return equals;
	}

	public int hashCode() {
		long leastSignificantLong = _lib.mpz_get_si(_mpz);
		return (int) (leastSignificantLong ^ (leastSignificantLong >>> 32));
	}

	public String toString() {
		return _lib.mpz_get_str(null, 10, _mpz);
	}

}

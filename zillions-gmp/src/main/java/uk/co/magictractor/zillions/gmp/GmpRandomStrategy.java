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

import static uk.co.magictractor.zillions.gmp.GmpLibInstance.__lib;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.create.RandomStrategy;
import uk.co.magictractor.zillions.core.environment.Init;
import uk.co.magictractor.zillions.gmp.struct.gmp_randstate_t;
import uk.co.magictractor.zillions.gmp.struct.mp_bitcnt_t;

public class GmpRandomStrategy implements RandomStrategy, Init {

	private final gmp_randstate_t _state = new gmp_randstate_t();
	private final GmpBigInt _rop  = new GmpBigInt();

	@Override
	public void init() throws Exception {
		//_state._mp_seed = _rop.getInternalValue();
		System.err.println("init");
		__lib.mpz_init(_state._mp_seed);
		__lib.gmp_randinit_default(_state);
		System.err.println("init done");
	}

	@Override
	public void setSeed(long seed) {
		//throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public BigInt random(int numBits) {
		//GmpLibInstance.__lib.mpz_urandomb(_rop.getInternalValue(), _state, new mp_bitcnt_t(numBits));
		return _rop;
	}

}

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
import uk.co.magictractor.zillions.core.create.RandomStrategy;

public class GmpJnaRandomStrategy implements RandomStrategy {

	@Override
	public void setSeed(long seed) {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public BigInt random(int numBits) {
		throw new UnsupportedOperationException("not yet implemented");
	}

	// Hmm. Might want to try JNAerator again?
	// https://github.com/nativelibs4java/JNAerator/wiki/Command-Line-Options-And-Environment-Variables

	// From randmt.h - THIS ISN'T gmp_randstate_t?
//	/* State structure for MT.  */
//	typedef struct
//	{
//	  gmp_uint_least32_t mt[N];    /* State array.  */
//	  int mti;                     /* Index of current value.  */
//	} gmp_rand_mt_struct;

// gmp_uint_least32_t

	// mp_bitcnt_t
}

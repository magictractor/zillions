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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.Union;

/**
 * Random state structure.
 *
 * From gmp.h
 *
 * <pre>
typedef struct
{
  mpz_t _mp_seed;           &#47;* _mp_d member points to state of the generator. *&#47;
  gmp_randalg_t _mp_alg;    &#47;* Currently unused. *&#47;
  union {
    void *_mp_lc;           &#47;* Pointer to function pointers structure.  *&#47;
  } _mp_algdata;
} __gmp_randstate_struct;

&#47;* Available random number generation algorithms.  *&#47;
typedef enum
{
  GMP_RAND_ALG_DEFAULT = 0,
  GMP_RAND_ALG_LC = GMP_RAND_ALG_DEFAULT &#47;* Linear congruential.  *&#47;
} gmp_randalg_t;
 * </pre>
 */
@FieldOrder({ "_mp_seed", "_mp_alg", "_mp_algdata" })
public class gmp_randstate_t extends Structure {

    public mpz_t _mp_seed;
    public int _mp_alg;
    public _mp_algdata_union _mp_algdata;

    public static class _mp_algdata_union extends Union {
        public Pointer _mp_lc;

        public _mp_algdata_union() {
            super();
        }
    }

}

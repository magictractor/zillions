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
package uk.co.magictractor.zillions.gmp.struct;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.NativeLongByReference;

/**
 <pre>
 typedef struct
{
  int _mp_alloc;                &#47;* Number of *limbs* allocated and pointed
                                   to by the _mp_d field.  *&#47;
  int _mp_size;                 &#47;* abs(_mp_size) is the number of limbs the
                                   last field points to.  If _mp_size is
                                   negative this is a negative number.  *&#47;
  mp_limb_t *_mp_d;             &#47;* Pointer to the limbs.  *&#47;
} __mpz_struct;
</pre>
 */
@FieldOrder({"_mp_alloc" , "_mp_size" , "_mp_d"})
public class mpz_t extends Structure
{
  /**
   * Number of *limbs* allocated and pointed<br> to by the _mp_d field.
   */
  public int _mp_alloc;
  /**
   * abs(_mp_size) is the number of limbs the<br> last field points to. If _mp_size is<br>
   * negative this is a negative number.
   */
  public int _mp_size;
  /**
   * Pointer to the limbs.<br> C type : mp_limb_t*
   */
  public NativeLongByReference _mp_d;

  // Mimic the macro used to implement mpz_sgn
  // #define mpz_sgn(Z) ((Z)->_mp_size < 0 ? -1 : (Z)->_mp_size > 0)
  public int mpz_sgn() {
	  return _mp_size == 0 ? 0 : (_mp_size > 0 ? 1 : -1); 
  }
}

package uk.co.magictractor.zillions.gmp.struct;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.Union;
import com.sun.jna.ptr.PointerByReference;

import uk.co.magictractor.zillions.gmp.GmpJnaBigInt;

/**
 * Random state structure.
 * 
 * From gmp.h
 * 
 * <pre>
typedef struct
{
  mpz_t _mp_seed;         	&#47;* _mp_d member points to state of the generator. 	&#47;
  gmp_randalg_t _mp_alg;  	&#47;* Currently unused. *	&#47;
  union {
    void *_mp_lc;         	&#47;* Pointer to function pointers structure.  *	&#47;
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
//@FieldOrder({ "_mp_seed" })
public class gmp_randstate_t extends Structure {
	// public mpz_t _mp_seed = new mpz_t();
	// TODO! does ths need to be initialized??
	public mpz_t _mp_seed = new GmpJnaBigInt().getInternalValue();

	// https://www.embedded.fm/blog/2016/6/28/how-big-is-an-enum
	// int for enum
	// https://stackoverflow.com/questions/1157817/how-to-map-enum-in-jna
	//public byte _mp_alg;
	public short _mp_alg;
	//public int _mp_alg;
	//public long _mp_alg;

	// public Pointer _mp_algdata;
	public mp_algdata _mp_algdata = new mp_algdata();

	public static final class mp_algdata extends Union {
		public mp_algdata() {
			setType(PointerByReference.class);
		}
		public PointerByReference _mp_algdata = new PointerByReference();
	}
}

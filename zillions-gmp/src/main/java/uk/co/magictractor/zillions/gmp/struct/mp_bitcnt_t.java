package uk.co.magictractor.zillions.gmp.struct;

import com.sun.jna.NativeLong;

/**
 * In gmp.h
 * 
 * <pre>
 * typedef  unsigned long int  mp_bitcnt_t;
 * </pre>
 */
public class mp_bitcnt_t extends NativeLong {

	private static final long serialVersionUID = 1L;

	// JNA needs a no-args constructor
	public mp_bitcnt_t() {
		super();
	}

	public mp_bitcnt_t forAnyBitNum(long bitNum) {
		return new mp_bitcnt_t(bitNum);
	}

	/** Used with set, clear, flip and test bit methods where negative bit numbers are not permitted.
	 * 
	 * The exception thrown is consistent with BigInteger.
	 */
	public static mp_bitcnt_t forPositiveBitNum(long bitNum) {
		if (bitNum < 0) {
			throw new ArithmeticException("Negative bit address");
		}
		return new mp_bitcnt_t(bitNum);
	}

	public mp_bitcnt_t(long bitNum) {
		super(bitNum, true);
	}

}

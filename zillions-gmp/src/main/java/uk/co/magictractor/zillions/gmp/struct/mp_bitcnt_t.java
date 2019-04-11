package uk.co.magictractor.zillions.gmp.struct;

import com.sun.jna.NativeLong;

/**
 * In gmp.h
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

	public mp_bitcnt_t(long value) {
		super(value, true);
	}

}

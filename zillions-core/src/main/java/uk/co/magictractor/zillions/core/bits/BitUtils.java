package uk.co.magictractor.zillions.core.bits;

public final class BitUtils {

	private BitUtils() {
	}

	// Based on Java's Bits class.
	public static int makeInt(byte b3, byte b2, byte b1, byte b0) {
		return (b3 << 24) | ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | (b0 & 0xff);
	}

	public static int makeInt(byte b2, byte b1, byte b0) {
		return ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | (b0 & 0xff);
	}

	public static int makeInt(byte b1, byte b0) {
		return ((b1 & 0xff) << 8) | (b0 & 0xff);
	}

	public static int makeInt(byte b0) {
		return b0 & 0xff;
	}

}

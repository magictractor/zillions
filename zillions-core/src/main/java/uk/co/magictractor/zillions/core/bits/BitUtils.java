package uk.co.magictractor.zillions.core.bits;

public final class BitUtils {

    private BitUtils() {
    }

    /**
     * Determines whether the bytes represent a negative number, assuming that the
     * bytes are a 2's complement representation with the most significant byte
     * first.
     */
    public static boolean isNegative(byte[] bytes) {
        return (bytes[0] & 0x80) != 0;
    }

    public static byte[] intsToBytes(int... bytesAsInts) {
        byte[] bytes = new byte[bytesAsInts.length];
        for (int i = 0; i < bytes.length; i++) {
            int byteAsInt = bytesAsInts[i];
            if ((byteAsInt & 0xffffff00) > 0) {
                throw new IllegalArgumentException();
            }
            bytes[i] = (byte) byteAsInt;
        }

        return bytes;
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

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
package uk.co.magictractor.zillions.core.bits;

/**
 * Utility methods for working with bits and bytes which may be useful to some
 * BitInt implementations.
 */
public final class BitUtils {

    public static final byte BYTE_00 = (byte) 0x00;
    public static final byte BYTE_FF = (byte) 0xff;

    private BitUtils() {
    }

    /**
     * Determines whether the bytes represent a negative number, assuming that
     * the bytes are a 2's complement representation with the most significant
     * byte first.
     */
    public static boolean isNegative(byte[] bytes) {
        return (bytes[0] & 0x80) != 0;
    }

    public static boolean isNegative(int[] bytesAsInts) {
        return (bytesAsInts[0] & 0x80) != 0;
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

    public static int[] bytesToInts(byte[] bytes) {
        int[] bytesAsInts = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytesAsInts[i] = bytes[i] & 0xff;
        }

        return bytesAsInts;
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

    /**
     * Set (up to) four bytes in an array corresponding to values in the given
     * int. The most significant bits in the int are set in the lower indexed
     * elements of the array. The index value may be -3, -2 or -1, in which case
     * the most significant bits of the int are not copied.
     *
     * @param bytes array in which values are set
     * @param index the first index in bytes in which a value is set, elements
     *        index+1, index+2 and index+3 also have values set
     * @param bytesAsInt contains four byte values to set in the array
     */
    public static void setBytes(byte[] bytes, int index, int bytesAsInt) {
        // TODO! refactor
        // TODO! perhaps allow least significant bytes to be clipped too

        if (index < -3) {
            throw new ArrayIndexOutOfBoundsException("index too low: " + index);
        }

        if (index >= 0) {
            bytes[index] = (byte) (bytesAsInt >> 24);
        }
        if (index >= -1) {
            bytes[index + 1] = (byte) (bytesAsInt >> 16);
        }
        if (index >= -2) {
            bytes[index + 2] = (byte) (bytesAsInt >> 8);
        }
        bytes[index + 3] = (byte) (bytesAsInt);
    }

}

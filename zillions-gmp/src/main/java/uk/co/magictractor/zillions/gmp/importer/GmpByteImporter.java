package uk.co.magictractor.zillions.gmp.importer;

import static uk.co.magictractor.zillions.gmp.GmpLibInstance.__lib;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.sun.jna.Memory;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.importer.ByteImporter;
import uk.co.magictractor.zillions.gmp.GmpBigInt;
import uk.co.magictractor.zillions.gmp.struct.mpz_t;

// TODO! can we avoid copying the bytes?
// https://stackoverflow.com/questions/5244214/get-pointer-of-byte-array-in-jna

public class GmpByteImporter implements ByteImporter {

	public BigInt from(BigInt rop, byte[] bytes) {

		Memory memory = new Memory(bytes.length);

		// GMP imports unsigned values, check and adjust if negative
		boolean isNegative = (bytes[0] & 0x80) != 0;
		if (!isNegative) {
			memory.write(0, bytes, 0, bytes.length);
		} else {
			for (int i = 0; i < bytes.length; i++) {
				memory.setByte(i, (byte) ~bytes[i]);
			}
		}

		mpz_t mpz = ((GmpBigInt) rop).getInternalValue();
		__lib.mpz_import(mpz, bytes.length, 1, 1, 1, 0, memory);

		if (isNegative) {
			__lib.mpz_com(mpz, mpz);
		}

		return rop;
	}

}

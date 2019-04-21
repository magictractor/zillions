package uk.co.magictractor.zillions.core.create;

import java.util.Random;


import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.environment.Environment;
import uk.co.magictractor.zillions.core.environment.Priority;
import uk.co.magictractor.zillions.core.importer.ByteImporter;

/**
 * Uses Java's Random class to generate random bytes which is then imported to
 * the BigInt implementation.
 * 
 * This allows the same "random" values to be used for benchmarking across all
 * BigInt implementations.
 */
public class JavaRandomStrategy implements RandomStrategy, Priority {

	private static final ByteImporter BYTE_IMPORTER = Environment.getBestAvailableImplementation(ByteImporter.class);
	
	private final Random randomNumberGenerator = new Random();

	@Override
	public int getPriority() {
		// Likely to be used only for testing and benchmarking.
		return Priority.SPECIFIC_USE;
	}

	@Override
	public BigInt randomise(BigInt rop, int numBits) {
		byte[] bytes = generateRandomBytes(numBits);
		return BYTE_IMPORTER.unsignedFrom(rop, bytes);
	}

	private byte[] generateRandomBytes(int numBits) {
		int byteCount = (numBits + 7) >> 3;
		byte[] bytes = new byte[byteCount];
		
		randomNumberGenerator.nextBytes(bytes);
		
		// Apply a mask to the most significant byte.
		int keepBits = numBits % 8;
		if (keepBits > 0) {
			int mask = (1 << keepBits) - 1;
			bytes[0] = (byte) (bytes[0] & mask);
		}
		
		return bytes;
	}

	@Override
	public void setSeed(long seed) {
		randomNumberGenerator.setSeed(seed);
	}

}

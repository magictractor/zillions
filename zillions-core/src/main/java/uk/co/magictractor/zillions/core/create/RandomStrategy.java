package uk.co.magictractor.zillions.core.create;

import uk.co.magictractor.zillions.core.BigInt;

public interface RandomStrategy {

	/**
	 * Set a seed for the random BigInts created by {@link #random}.
	 * 
	 * The same seed will create the same random numbers only within single
	 * CreateStrategy implementations.
	 * 
	 * @param seed
	 */
	void setSeed(long seed);

	/**
	 * <p>
	 * Modify and return the given BigInt with a uniformly random value up to the
	 * given number of bits
	 * <p>
	 * Note that the most significant bits may be zero. If creating a test which
	 * requires a very large random number, this can be combined with setBit() to
	 * ensure that the test doesn't accidentally create a (relatively) small number.
	 * 
	 * @return a uniformly random BigInt in the range 0 to 2^n-1 (inclusive)
	 */
	BigInt randomise(BigInt rop, int numBits);

}

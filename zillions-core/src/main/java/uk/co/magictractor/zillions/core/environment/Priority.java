package uk.co.magictractor.zillions.core.environment;

@FunctionalInterface
public interface Priority {

	/**
	 * An implemention provided by an underlying library which is likely to be
	 * efficient.
	 * 
	 * The implementing strategy often only applies to one BigInt implementation.
	 */
	public final static int NATIVE_IMPLEMENTATION = 500000;

	/**
	 * The default priority assigned to strategy implementations which do not
	 * implement the Priority interface.
	 */
	public final static int DEFAULT = 0;

	/**
	 * A simple implementation, which is likely to perform badly, especially with
	 * larger numbers. Often applicable across all BigInt implementations.
	 * 
	 * Perhaps created for benchmarking, or a reference implementation.
	 * 
	 * Other implementations of the same stragety are probably better.
	 * 
	 * Have avoided the word "naive", because it's too close to "native".
	 */
	public final static int POOR_PERFORMANCE = -500000;

	/**
	 * Very low priority. Should only be used if no other implementations of the
	 * strategy are available.
	 */
	public final static int ERROR_FALLBACK = -900000;

	/**
	 * Although implementations of this method will almost always be trivial, this
	 * method should only be called by StrategyHolder. Other code can query the
	 * priority via the StrategyHolder. Doing so handles strategies which do not
	 * implement this interface, and strategies which throw an exception when this
	 * method is called (which makes the strategy unavailable).
	 * 
	 * @return usually a constant value, one of the constants in the Priority class
	 *         or a value based on a value there with an offset
	 */
	public int getPriority();

}

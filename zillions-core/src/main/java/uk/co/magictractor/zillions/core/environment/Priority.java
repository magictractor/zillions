
package uk.co.magictractor.zillions.core.environment;

@FunctionalInterface
public interface Priority {

    /**
     * An implemention provided by an underlying library which is likely to be
     * efficient.
     * 
     * The implementing strategy often only applies to one BigInt implementation.
     */
    int NATIVE_IMPLEMENTATION = 500000;

    /**
     * The default priority assigned to strategy implementations which do not
     * implement the Priority interface.
     */
    int DEFAULT = 0;

    /**
     * Not recommendend for general use, there should be better implementations
     * available.
     * 
     * Services with this priority often require application code to specifically
     * set a property to choose the service implementation.
     * 
     * Used for a random strategy which is preferred for unit and benchmark tests,
     * but is otherwise not recommended.
     */
    int SPECIFIC_USE = -200000;

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
    int POOR_PERFORMANCE = -500000;

    /**
     * Very low priority. Should only be used if no other implementations of the
     * strategy are available.
     */
    int ERROR_FALLBACK = -900000;

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
    int getPriority();

}

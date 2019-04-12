package uk.co.magictractor.zillions.core.environment;

/**
 * TODO! revisit this. Was on create strategy, moved here with the intention
 * that it is only implemented if the strategy implementation may be unavailable
 * due to issues with external resources such as C libararies.
 */
public interface Available {

	/**
	 * <p>
	 * A typical reason for not being available is that a required native library is
	 * not available.
	 * </p>
	 * <p>
	 * If this method returns false it is recommended that information is logged to
	 * indicate the reason. The BigInt framework should only call this method once,
	 * avoiding noise in logs.
	 * </p>
	 * 
	 * @return true if is this strategy is available for use; false otherwise
	 */
	// TODO! return a reason rather than a flag
	boolean isAvailable();

}

package uk.co.magictractor.zillions.junit.extension;

public interface ValueChange {

    /**
     * Apply the value change.
     */
    void apply();

    /**
     * <p>
     * Revert the value change.
     * </p>
     * <p>
     * The original value should be captured when {@link #apply} is called
     * rather than when the ValueChange is constructed.
     * </p>
     */
    void revert();

}

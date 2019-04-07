package uk.co.magictractor.zillions.core;

public interface EnhancedComparable<T> extends Comparable<T> {

	default boolean isLessThan(T other) {
		return compareTo(other) < 0;
	}

	default boolean isLessThanOrEqual(T other) {
		return compareTo(other) <= 0;
	}

	default boolean isGreaterThan(T other) {
		return compareTo(other) > 0;
	}

	default boolean isGreaterThanOrEqual(T other) {
		return compareTo(other) >= 0;
	}

}

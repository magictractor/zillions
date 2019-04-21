package uk.co.magictractor.zillions.testbed;

import uk.co.magictractor.zillions.core.environment.Environment;

public abstract class AbstractStrategyTest<T> {

	private final T _impl;

	protected AbstractStrategyTest(Class<? extends T> clazz) {
		if (clazz.isInterface()) {
			_impl = Environment.getBestAvailableImplementation(clazz);
		} else {
			try {
				_impl = clazz.newInstance();
			} catch (ReflectiveOperationException e) {
				throw new IllegalArgumentException();
			}
		}
	}

	protected T getImpl() {
		return _impl;
	}
}

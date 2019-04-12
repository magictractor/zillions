package uk.co.magictractor.zillions.core.proxy;

import java.lang.reflect.Method;

import uk.co.magictractor.zillions.core.environment.Priority;

/**
 * Creates a very low priority strategy implementation which throws an exception
 * containing useful information when no other strategy implementation can be
 * found.
 */
public class MissingImplStrategyFactory extends AbstractProxyStrategyFactory {

	public MissingImplStrategyFactory() {
		addInterface(Priority.class, () -> {
			return Priority.ERROR_FALLBACK;
		});
	}

	@Override
	Object handle(Class<?> apiClass, Method method, Object[] args) {
		throw new UnsupportedOperationException(
				"No implementation found for " + apiClass.getSimpleName() + "  [method=" + method + "]");
	}

}

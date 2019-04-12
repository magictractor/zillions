package uk.co.magictractor.zillions.core.proxy;

import java.lang.reflect.Method;

public abstract class AbstractDelegatingStrategyFactory extends AbstractProxyStrategyFactory {

	protected final Object handle(Class<?> apiClass, Method method, Object[] args) throws Throwable {
		Object delegate = findDelegate(apiClass);
		return method.invoke(delegate, args);
	}

	protected abstract <S> S findDelegate(Class<S> apiClass);

}
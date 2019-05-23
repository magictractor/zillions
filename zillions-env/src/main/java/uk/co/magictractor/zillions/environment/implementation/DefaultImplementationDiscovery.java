package uk.co.magictractor.zillions.environment.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import com.google.common.base.MoreObjects;

import uk.co.magictractor.zillions.api.Init;
import uk.co.magictractor.zillions.environment.Environment;
import uk.co.magictractor.zillions.environment.property.PropertyDiscovery;

/**
 * <ol>
 * <li>If there is a system property with name "{apiClassName}.impl" then the
 * system property's value is the class name of the api implementation to be
 * used. The implementation class is expected to have a public no-args
 * constructor.
 * </p>
 * <code>foo.bar.Interface.impl=foo.bar.Implementation</code>
 * <p>
 * </li>
 * <li>Check SPI for an implementation. If multiple implementations are found,
 * an exception is thrown.</li>
 * </ol>
 * This strategy is always used at least once, to find the Discovery strategy
 * and Property strategy implementations to be used. For those, if no
 * implementations for those are found, then DefaultDiscovery and DefaultPropery
 * are used.
 */
public class DefaultImplementationDiscovery implements ImplementationDiscovery {

    private final PropertyDiscovery _properties = initProperties();

    /**
     * Cache may contain null values for APIs where no implementating class was
     * found.
     */
    private final Map<Class<?>, Object> _cache = new HashMap<>();

    protected PropertyDiscovery initProperties() {
        return Environment.getProperties();
    }

    @Override
    public <T> Optional<T> findOptionalImplementation(Class<T> apiClass) {
        T impl;
        if (_cache.containsKey(apiClass)) {
            impl = (T) _cache.get(apiClass);
        }
        else {
            impl = discover(apiClass);
            if (impl != null) {
                init(impl);
            }
            _cache.put(apiClass, impl);
        }

        return Optional.ofNullable(impl);
    }

    private <T> T discover(Class<T> apiClass) {
        T impl;

        impl = discoverImplementationViaSystemProperty(apiClass);
        if (impl != null) {
            return impl;
        }

        impl = discoverImplementationViaSpi(apiClass);

        return impl;
    }

    private <T> void init(T impl) {
        if (impl instanceof Init) {
            ((Init) impl).init();
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T discoverImplementationViaSystemProperty(Class<T> apiClass) {
        String implName = _properties.getString(apiClass, "impl", null);
        if (implName == null) {
            return null;
        }

        try {
            Class<?> implClass = Class.forName(implName);
            return (T) implClass.newInstance();
        }
        catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    protected <T> T discoverImplementationViaSpi(Class<T> apiClass) {
        T impl = null;
        List<T> spiList = spiLoadList(apiClass);
        if (spiList.size() == 1) {
            impl = spiList.get(0);
        }
        else if (spiList.size() > 1) {
            // TODO! exceptions which point at a web page containing more information
            throw new IllegalStateException("There are multiple SPI implementations for " + apiClass.getName()
                    + ". The project is either misconfigured, or should use a system property to specify the implementation to be used.");
        }

        return impl;
    }

    protected <T> List<T> spiLoadList(Class<T> apiClass) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(apiClass);
        List<T> spiList = new ArrayList<>();
        serviceLoader.iterator().forEachRemaining(i -> spiList.add(i));
        return spiList;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }

}

package uk.co.magictractor.zillions.environment;

import uk.co.magictractor.zillions.api.factory.ImplementationFactory;
import uk.co.magictractor.zillions.environment.property.DefaultPropertyDiscovery;
import uk.co.magictractor.zillions.environment.property.PropertyDiscovery;

/**
 * ImplementationDiscovery implementation which is used once at startup to find
 * the ImplementationDiscovery and PropertyDiscovery implementations.
 */
public class BootstrapImplementationDiscovery extends DefaultImplementationDiscovery {

    @Override
    protected PropertyDiscovery initProperties() {
        return new DefaultPropertyDiscovery();
    }

    @Override
    protected ImplementationFactory initFactory() {
        // No ImplementationFactory during bootstrap.
        return null;
    }

    @Override
    protected <T> T createMissingImplementation(Class<T> apiClass) {
        return null;
    }

}

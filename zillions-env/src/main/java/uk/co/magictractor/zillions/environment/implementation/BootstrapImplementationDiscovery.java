package uk.co.magictractor.zillions.environment.implementation;

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

}

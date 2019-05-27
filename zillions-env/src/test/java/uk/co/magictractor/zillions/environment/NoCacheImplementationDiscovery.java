package uk.co.magictractor.zillions.environment;

import java.util.Map;

public class NoCacheImplementationDiscovery extends DefaultImplementationDiscovery {

    @Override
    protected Map<Class<?>, Object> initCache() {
        return new NoOpMap<>();
    }

}

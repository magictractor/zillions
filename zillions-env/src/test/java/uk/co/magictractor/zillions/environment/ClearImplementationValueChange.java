package uk.co.magictractor.zillions.environment;

import java.util.Arrays;
import java.util.List;

import uk.co.magictractor.zillions.junit.extension.ValueChange;

public class ClearImplementationValueChange implements ValueChange {

    private final List<Class<?>> _apiClasses;

    public ClearImplementationValueChange(Class<?>... apiClasses) {
        _apiClasses = Arrays.asList(apiClasses);
    }

    @Override
    public void apply() {
        // Do nothing.
    }

    @Override
    public void revert() {
        // trying NoCacheImplementationDiscovery instead
        //  _apiClasses.forEach(Environment::removeFromCache);
    }

}

package uk.co.magictractor.zillions.environment;

import uk.co.magictractor.zillions.junit.extension.AbstractValueChangeExtension;

public class EnvironmentExtension extends AbstractValueChangeExtension {

    public EnvironmentExtension withImplementationsRemovedAfter(Class<?>... apiClasses) {
        return this;
    }

    public void setImplementationsRemovedAfter(Class<?>... apiClasses) {
        addValueChange(new ClearImplementationValueChange(apiClasses));
    }
}

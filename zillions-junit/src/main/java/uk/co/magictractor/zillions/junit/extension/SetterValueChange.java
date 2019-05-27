package uk.co.magictractor.zillions.junit.extension;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SetterValueChange<T> implements ValueChange {

    private final Function<T, T> _setter;
    private final T _newValue;
    private T _originalValue;

    public SetterValueChange(Supplier<T> getter, Consumer<T> setter, T newValue) {
        _setter = value -> {
            T previousValue = getter.get();
            setter.accept(value);
            return previousValue;
        };
        _newValue = newValue;
    }

    public SetterValueChange(Function<T, T> setter, T newValue) {
        _setter = setter;
        _newValue = newValue;
    }

    @Override
    public void apply() {
        _originalValue = _setter.apply(_newValue);
    }

    @Override
    public void revert() {
        _setter.apply(_originalValue);
    }

}

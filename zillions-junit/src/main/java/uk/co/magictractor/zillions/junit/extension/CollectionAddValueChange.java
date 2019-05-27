package uk.co.magictractor.zillions.junit.extension;

import java.util.Collection;

public class CollectionAddValueChange<T> implements ValueChange {

    private final Collection<T> _collection;
    private final T _addValue;
    private boolean _alreadyContained;

    public CollectionAddValueChange(Collection<T> collection, T addValue) {
        _collection = collection;
        _addValue = addValue;
    }

    @Override
    public void apply() {
        _alreadyContained = _collection.contains(_addValue);
        if (_alreadyContained) {
            _collection.add(_addValue);
            System.out.println("added value " + _addValue);
        }
    }

    @Override
    public void revert() {
        if (!_alreadyContained) {
            _collection.remove(_addValue);
            System.out.println("removed value " + _addValue);
        }
    }

}

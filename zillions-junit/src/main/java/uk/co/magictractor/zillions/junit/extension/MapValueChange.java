package uk.co.magictractor.zillions.junit.extension;

import java.util.Map;

public class MapValueChange<K, V> implements ValueChange {

    private final Map<K, V> _map;
    private final K _key;
    private final V _value;
    private boolean _containsKey;
    private V _originalValue;

    public MapValueChange(Map<K, V> map, K key, V value) {
        _map = map;
        _key = key;
        _value = value;
    }

    @Override
    public void apply() {
        _containsKey = _map.containsKey(_key);
        if (_containsKey) {
            _originalValue = _map.get(_key);
        }
        _map.put(_key, _value);
    }

    @Override
    public void revert() {
        if (_containsKey) {
            _map.put(_key, _originalValue);
        }
        else {
            _map.remove(_key);
        }
    }

}

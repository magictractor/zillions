package uk.co.magictractor.zillions.junit.extension;

public class SystemPropertyValueChange implements ValueChange {

    private final String _key;
    private final String _newValue;
    private String _originalValue;
    // TODO! bin this
    private boolean _isApplied;

    public SystemPropertyValueChange(String key, String newValue) {
        _key = key;
        _newValue = newValue;
    }

    @Override
    public void apply() {
        _originalValue = System.setProperty(_key, _newValue);
        _isApplied = true;
    }

    @Override
    public void revert() {
        if (!_isApplied) {
            throw new IllegalStateException("not applied");
        }

        if (_originalValue == null) {
            System.err.println("revert " + _key + " cleared");
            System.clearProperty(_key);
        }
        else {
            System.err.println("revert " + _key + " -> " + _originalValue);
            System.setProperty(_key, _originalValue);
        }
    }

}

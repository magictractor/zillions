/**
 * Copyright 2015-2019 Ken Dobson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

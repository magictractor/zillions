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
package uk.co.magictractor.zillions.testbed.suite.filter;

import org.junit.platform.engine.Filter;
import org.junit.platform.engine.FilterResult;

public class SamePackageFilter implements Filter<String> {

    private final String _suiteClassName;
    private final String _suitePackagePlusDot;
    private FilterResult _included = FilterResult.included("is in the same package as the suite class");
    private FilterResult _excluded = FilterResult.excluded("is in a different package than the suite class");

    public SamePackageFilter(String suiteClassName) {
        _suiteClassName = suiteClassName;
        _suitePackagePlusDot = suiteClassName.substring(0, suiteClassName.lastIndexOf(".") + 1);
    }

    public SamePackageFilter(Class<?> suiteClass) {
        this(suiteClass.getName());
    }

    @Override
    public FilterResult apply(String testClassName) {
        return isIncluded(testClassName) ? _included : _excluded;
    }

    private boolean isIncluded(String testClassName) {
        return testClassName.startsWith(_suitePackagePlusDot)
                && testClassName.indexOf(".", _suitePackagePlusDot.length()) == -1
                && !_suiteClassName.equals(testClassName);
    }

}

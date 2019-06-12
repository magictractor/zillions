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

public class ChildPackageFilter implements Filter<String> {

    private final String _suitePackagePlusDot;
    private FilterResult _included = FilterResult.included("is in a child package of the suite class");
    private FilterResult _excluded = FilterResult.excluded("is in a package which is not a child of the suite class");

    public ChildPackageFilter(String suiteClassName) {
        _suitePackagePlusDot = suiteClassName.substring(0, suiteClassName.lastIndexOf(".") + 1);
    }

    public ChildPackageFilter(Class<?> suiteClass) {
        this(suiteClass.getName());
    }

    @Override
    public FilterResult apply(String testClassName) {
        return isIncluded(testClassName) ? _included : _excluded;
    }

    private boolean isIncluded(String testClassName) {
        if (!testClassName.startsWith(_suitePackagePlusDot)) {
            return false;
        }

        int lastDotIndex = testClassName.indexOf(".", _suitePackagePlusDot.length());
        if (lastDotIndex == -1) {
            // Same package.
            return false;
        }

        // Check for decendants beneath child packages.
        return testClassName.indexOf(".", lastDotIndex + 1) == -1;
    }

}

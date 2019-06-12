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

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.platform.engine.Filter;
import org.junit.platform.engine.FilterResult;

public class IsSuiteFilter implements Filter<String> {

    private final Supplier<Predicate<String>> _suitePredicateSupplier;
    private FilterResult _included = FilterResult.included("is a suite");
    private FilterResult _excluded = FilterResult.excluded("is not a suite");

    public IsSuiteFilter(Supplier<Predicate<String>> suitePredicateSupplier) {
        _suitePredicateSupplier = suitePredicateSupplier;
    }

    @Override
    public FilterResult apply(String testClassName) {
        return isIncluded(testClassName) ? _included : _excluded;
    }

    private boolean isIncluded(String testClassName) {
        return _suitePredicateSupplier.get().test(testClassName);
    }

}

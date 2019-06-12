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
package uk.co.magictractor.zillions.testbed.dynamic;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.Filter;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.PackageNameFilter;
import org.junit.platform.launcher.EngineFilter;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TagFilter;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.suite.api.ExcludeClassNamePatterns;
import org.junit.platform.suite.api.ExcludeEngines;
import org.junit.platform.suite.api.ExcludePackages;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;

import uk.co.magictractor.zillions.testbed.suite.filter.ChildPackageFilter;
import uk.co.magictractor.zillions.testbed.suite.filter.SamePackageFilter;

public class SuiteRequestBuilder {

    private final String _suiteClassName;

    private List<DiscoverySelector> _discoverySelectors = new ArrayList<>();

    private Filter<?> _packageFilter;

    /**
     * Filters which are applied to test classes only. These filters are not
     * applied to nested suites. Whether a test represents a suite is determined
     * by _suitePredicate.
     */
    private List<Filter<?>> _testFilters = new ArrayList<>();
    //private DiscoveryFilter<String> _isSuiteFilter = (s) -> s.endsWith("Suite") ? FilterResult.included("is a suite class") : FilterResult.excluded("is a suite class")

    public SuiteRequestBuilder(Class<?> suiteClass) {
        _suiteClassName = suiteClass.getName();
        readAnnotations(suiteClass);
    }

    public LauncherDiscoveryRequest build() {
        LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();

        if (_discoverySelectors.isEmpty()) {
            throw new IllegalStateException("There are no DiscoverySelectors - the suite is misconfigured");
        }
        builder.selectors(_discoverySelectors);

        // Handle nested suites
        // TODO! this could be a smidge more efficient
        for (Filter<?> filter : _testFilters) {
            builder.filters(filter);
        }

        return builder.build();
    }

    private void readAnnotations(Class<?> suiteClass) {
        _testFilters = new ArrayList<>();

        // Similar to JUnitPlatform.addFiltersFromAnnotations()
        handleFilterAnnotation(suiteClass, ExcludeClassNamePatterns.class,
            a -> ClassNameFilter.excludeClassNamePatterns(a.value()));
        handleFilterAnnotation(suiteClass, IncludeClassNamePatterns.class,
            a -> ClassNameFilter.includeClassNamePatterns(a.value()));
        handleFilterAnnotation(suiteClass, ExcludePackages.class,
            a -> PackageNameFilter.excludePackageNames(a.value()));
        handleFilterAnnotation(suiteClass, IncludePackages.class,
            a -> PackageNameFilter.includePackageNames(a.value()));
        handleFilterAnnotation(suiteClass, ExcludeTags.class, a -> TagFilter.excludeTags(a.value()));
        handleFilterAnnotation(suiteClass, IncludeTags.class, a -> TagFilter.includeTags(a.value()));
        handleFilterAnnotation(suiteClass, ExcludeEngines.class, a -> EngineFilter.excludeEngines(a.value()));
        handleFilterAnnotation(suiteClass, IncludeEngines.class, a -> EngineFilter.includeEngines(a.value()));

        handleAnnotation(suiteClass, SelectClasses.class,
            a -> addDiscoverySelectors(a.value(), v -> DiscoverySelectors.selectClass(v)));
        handleAnnotation(suiteClass, SelectPackages.class,
            a -> addDiscoverySelectors(a.value(), v -> DiscoverySelectors.selectPackage(v)));

        // very temporary!!
        //        if (!_testFilters.isEmpty()) {
        //            throw new IllegalStateException("Suite annotations should be removed for now");
        //        }

    }

    public void addSamePackageFilter() {
        //        Filter<String> filter = className -> isSamePackage(className)
        //                ? FilterResult.included("is in the same package as the suite class")
        //                : FilterResult.excluded("is in a difference package than the suite class");
        addPackageFilter(new SamePackageFilter(_suiteClassName));
    }

    public void addChildPackageFilter() {
        //        Filter<String> filter = className -> isChildPackage(className)
        //                ? FilterResult.included("is in a child package of the suite class")
        //                : FilterResult.excluded("is in a package which is not a child of the suite class");
        addPackageFilter(new ChildPackageFilter(_suiteClassName));
    }

    private void addPackageFilter(Filter<?> filter) {
        if (_packageFilter != null) {
            throw new IllegalStateException("package filter has already been set");
        }
    }

    //    private boolean isSamePackage(String className) {
    //        return className.startsWith(_suiteClassName + ".");
    //    }

    //    private boolean isChildPackage(String className) {
    //        return true;
    //    }

    private <A extends Annotation> void handleFilterAnnotation(Class<?> suiteClass, Class<A> annotationClass,
            Function<A, Filter<?>> filterFunction) {
        handleAnnotation(suiteClass, annotationClass, (a) -> {
            _testFilters.add(filterFunction.apply(a));
        });
    }

    private <V> void addDiscoverySelectors(V[] values, Function<V, DiscoverySelector> selectorFunction) {
        for (V value : values) {
            _discoverySelectors.add(selectorFunction.apply(value));
        }
    }

    private <A extends Annotation> void handleAnnotation(Class<?> suiteClass, Class<A> annotationClass,
            Consumer<A> annotationConsumer) {
        A annotation = suiteClass.getAnnotation(annotationClass);
        if (annotation != null) {
            annotationConsumer.accept(annotation);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[suiteClass=" + _suiteClassName + "]";
    }

}

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
package uk.co.magictractor.zillions.testbed.suite;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicNode;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.Filter;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.PackageNameFilter;
import org.junit.platform.launcher.EngineFilter;
import org.junit.platform.launcher.TagFilter;
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
import uk.co.magictractor.zillions.testbed.suite.filter.IsSuiteFilter;
import uk.co.magictractor.zillions.testbed.suite.filter.SamePackageFilter;

/**
 * <p>
 * Runs a JUnit suite as dynamic tests without having to change the test engine.
 * Supports most JUnit suite associated annotations, although support is also
 * provided for creating suites without annotations.
 * </p>
 * <p>
 * Currently these annotations are supported
 * <ul>
 * <li>@SelectClasses</li>
 * <li>@SelectPackage</li>
 * <li>@IncludeClassNamePatterns</li>
 * <li>@ExcludeClassNamePatterns</li>
 * <li>@IncludePackages</li>
 * <li>@ExcludePackages</li>
 * <li>@IncludeTags</li>
 * <li>@ExcludeTags</li>
 * <li>@IncludeEngines</li>
 * <li>@ExcludeEngines</li>
 * </ul>
 * Standard annotations which are NOT currently supported are
 * <ul>
 * <li>TODO</li>
 * </ul>
 * <p>
 * This class will probably be removed once JUnit5 has better support for
 * suites. See https://github.com/junit-team/junit5/issues/744.
 * <ul>
 * <li>TODO! registered extensions should apply to nested suites too (see
 * extension in Attic)</li>
 * </ul>
 */
public class DynamicSuite {

    private final Class<?> _suiteClass;

    private List<DiscoverySelector> _discoverySelectors = new ArrayList<>();

    /**
     * Filters which are applied to test classes only. These filters are not
     * applied to nested suites. Whether a test represents a suite is determined
     * by _suitePredicate.
     */
    private List<Filter<?>> _testFilters = new ArrayList<>();

    private boolean _includeAnnotations = true;
    private Predicate<String> _isSuitePredicate = (s) -> s.endsWith("Suite");

    /**
     * There is no <code>DynamicSuite(Object suite)</code> constructor because
     * subclasses on the suite may be in a different package.
     */
    public DynamicSuite(Class<?> suiteClass) {
        _suiteClass = suiteClass;
    }

    public Class<?> getSuiteClass() {
        return _suiteClass;
    }

    public List<DiscoverySelector> getDiscoverySelectors() {
        return _discoverySelectors;
    }

    public List<Filter<?>> getTestFilters() {
        return _testFilters;
    }

    private Predicate<String> isSuitePredicate() {
        return _isSuitePredicate;
    }

    public DynamicSuite selectOthersInPackage() {
        _discoverySelectors.add(DiscoverySelectors.selectPackage(getSuitePackage()));
        _testFilters.add(new SamePackageFilter(_suiteClass));
        return this;
    }

    public DynamicSuite selectSuitesInChildPackages() {
        _discoverySelectors.add(DiscoverySelectors.selectPackage(getSuitePackage()));
        _testFilters.add(new ChildPackageFilter(_suiteClass));
        _testFilters.add(new IsSuiteFilter(this::isSuitePredicate));
        return this;
    }

    public DynamicSuite withSuitePredicate(Predicate<String> suitePredicate) {
        _isSuitePredicate = suitePredicate;
        return this;
    }

    public DynamicSuite withoutAnnotations() {
        _includeAnnotations = false;
        return this;
    }

    public DynamicSuite withAnnotations() {
        _includeAnnotations = true;
        return this;
    }

    private String getSuitePackage() {
        return _suiteClass.getPackage().getName();
    }

    public Stream<DynamicNode> stream() {
        // TODO!  guard against reading annotations multiple times
        // if stream() is called more than once
        if (_includeAnnotations) {
            readAnnotations();
        }

        return new DynamicSuiteExecutor().execute(this);
    }

    private void readAnnotations() {
        // Similar to JUnitPlatform.addFiltersFromAnnotations()
        handleFilterAnnotation(ExcludeClassNamePatterns.class,
            a -> ClassNameFilter.excludeClassNamePatterns(a.value()));
        handleFilterAnnotation(IncludeClassNamePatterns.class,
            a -> ClassNameFilter.includeClassNamePatterns(a.value()));
        handleFilterAnnotation(ExcludePackages.class,
            a -> PackageNameFilter.excludePackageNames(a.value()));
        handleFilterAnnotation(IncludePackages.class,
            a -> PackageNameFilter.includePackageNames(a.value()));
        handleFilterAnnotation(ExcludeTags.class, a -> TagFilter.excludeTags(a.value()));
        handleFilterAnnotation(IncludeTags.class, a -> TagFilter.includeTags(a.value()));
        handleFilterAnnotation(ExcludeEngines.class, a -> EngineFilter.excludeEngines(a.value()));
        handleFilterAnnotation(IncludeEngines.class, a -> EngineFilter.includeEngines(a.value()));

        handleAnnotation(SelectClasses.class,
            a -> addDiscoverySelectors(a.value(), v -> DiscoverySelectors.selectClass(v)));
        handleAnnotation(SelectPackages.class,
            a -> addDiscoverySelectors(a.value(), v -> DiscoverySelectors.selectPackage(v)));
    }

    private <A extends Annotation> void handleFilterAnnotation(Class<A> annotationClass,
            Function<A, Filter<?>> filterFunction) {
        handleAnnotation(annotationClass, (a) -> {
            _testFilters.add(filterFunction.apply(a));
        });
    }

    private <V> void addDiscoverySelectors(V[] values, Function<V, DiscoverySelector> selectorFunction) {
        for (V value : values) {
            _discoverySelectors.add(selectorFunction.apply(value));
        }
    }

    private <A extends Annotation> void handleAnnotation(Class<A> annotationClass,
            Consumer<A> annotationConsumer) {
        A annotation = _suiteClass.getAnnotation(annotationClass);
        if (annotation != null) {
            System.err.println("Handling annotation " + annotation);
            annotationConsumer.accept(annotation);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[suiteClass=" + _suiteClass.getName() + "]";
    }

}

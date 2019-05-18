package uk.co.magictractor.zillions.testbed.dynamic;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

public class SuiteRequestBuilder {

    private final Class<?> _suiteClass;
    private final SuiteRequestBuilder _parentBuilder;
    private List<Filter<?>> _filters;

    public SuiteRequestBuilder(Class<?> suiteClass, SuiteRequestBuilder parentBuilder) {
        _suiteClass = suiteClass;
        _parentBuilder = parentBuilder;
    }

    public LauncherDiscoveryRequest build() {
        LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();

        addSelectors(builder);

        // TODO! this could be a smidge more efficient
        for (Filter<?> filter : filters()) {
            builder.filters(filter);
        }

        return builder.build();
    }

    private void addSelectors(LauncherDiscoveryRequestBuilder builder) {
        boolean hasSelect = false;

        SelectClasses selectClasses = _suiteClass.getAnnotation(SelectClasses.class);
        if (selectClasses != null) {
            hasSelect = true;
            for (Class<?> selectClass : selectClasses.value()) {
                builder.selectors(DiscoverySelectors.selectClass(selectClass));
            }
        }

        SelectPackages selectPackages = _suiteClass.getAnnotation(SelectPackages.class);
        if (selectPackages != null) {
            hasSelect = true;
            for (String selectPackage : selectPackages.value()) {
                builder.selectors(DiscoverySelectors.selectPackage(selectPackage));
            }
        }

        if (!hasSelect) {
            throw new IllegalStateException(
                _suiteClass + " is used as a suite but is not annotated with @SelectClasses or @SelectPackages");
        }
    }

    public List<Filter<?>> filters() {
        if (_filters == null) {
            initFilters();
        }
        return _filters;
    }

    private void initFilters() {
        _filters = new ArrayList<>();
        if (_parentBuilder != null) {
            _filters.addAll(_parentBuilder.filters());
        }

        // Similar to JUnitPlatform.addFiltersFromAnnotations()
        addFilter(ExcludeClassNamePatterns.class, a -> ClassNameFilter.excludeClassNamePatterns(a.value()));
        addFilter(IncludeClassNamePatterns.class, a -> ClassNameFilter.includeClassNamePatterns(a.value()));
        addFilter(ExcludePackages.class, a -> PackageNameFilter.excludePackageNames(a.value()));
        addFilter(IncludePackages.class, a -> PackageNameFilter.includePackageNames(a.value()));
        addFilter(ExcludeTags.class, a -> TagFilter.excludeTags(a.value()));
        addFilter(IncludeTags.class, a -> TagFilter.includeTags(a.value()));
        addFilter(ExcludeEngines.class, a -> EngineFilter.excludeEngines(a.value()));
        addFilter(IncludeEngines.class, a -> EngineFilter.includeEngines(a.value()));
    }

    // ExcludeClassNamePatterns
    private <A extends Annotation> void addFilter(Class<A> annotationClass, Function<A, Filter<?>> filterFunction) {
        // ClassNameFilter.excludeClassNamePatterns(patterns);
        A annotation = _suiteClass.getAnnotation(annotationClass);
        if (annotation == null) {
            return;
        }

        Filter<?> filter = filterFunction.apply(annotation);
        _filters.add(filter);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[suiteClass=" + _suiteClass + "]";
    }

    //        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
    //                .selectors(DiscoverySelectors.selectClass(testClass))
    //                .build();

    //  private static final class SuiteFilters {
    //  private ClassNameFilter _excludeClassNamePatterns;
    //
    //  SuiteFilters(Class<?> suiteClass) {
    //      if (suiteClass.isAnnotationPresent(ExcludeClassNamePatterns.class)) {
    //          String[] patterns = suiteClass.getAnnotation(ExcludeClassNamePatterns.class).value();
    //          _excludeClassNamePatterns = ClassNameFilter.excludeClassNamePatterns(patterns);
    //      }
    //  }
    //
    //  Predicate<Class<?>> getClassPredicate() {
    //      if (_excludeClassNamePatterns != null) {
    //          // TODO! should this be name or simple name?
    //          return c -> _excludeClassNamePatterns.apply(c.getName()).included();
    //      }
    //      else {
    //          // No filter.
    //          return c -> true;
    //      }
    //  }
    //}

}

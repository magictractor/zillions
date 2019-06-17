package uk.co.magictractor.zillions.suite.filter;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.platform.engine.Filter;
import org.junit.platform.engine.FilterResult;

public class IfElseFilter<T> implements Filter<T> {

    private final Supplier<Predicate<T>> _ifPredicateSupplier;

    /** FilterResult returned when the _if Predicate is satisfied. */
    private final Filter<T> _thenFilter;

    /** Filter which is applied when the _if Predicate is not satisfied. */
    private final Filter<T> _elseFilter;

    public IfElseFilter(Supplier<Predicate<T>> ifPredicateSupplier, FilterResult thenResult, Filter<T> elseFilter) {
        _ifPredicateSupplier = ifPredicateSupplier;
        _thenFilter = (object) -> thenResult;
        _elseFilter = elseFilter;
    }

    public IfElseFilter(Supplier<Predicate<T>> ifPredicateSupplier, Filter<T> thenFilter, FilterResult elseResult) {
        _ifPredicateSupplier = ifPredicateSupplier;
        _thenFilter = thenFilter;
        _elseFilter = (object) -> elseResult;
    }

    public IfElseFilter(Supplier<Predicate<T>> ifPredicateSupplier, Filter<T> thenFilter, Filter<T> elseFilter) {
        _ifPredicateSupplier = ifPredicateSupplier;
        _thenFilter = thenFilter;
        _elseFilter = elseFilter;
    }

    @Override
    public FilterResult apply(T object) {
        if (_ifPredicateSupplier.get().test(object)) {
            System.err.println("then");
            return _thenFilter.apply(object);
        }
        else {
            System.err.println("else");
            return _elseFilter.apply(object);
        }
    }

}

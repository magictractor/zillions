package uk.co.magictractor.zillions.core.proxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import uk.co.magictractor.zillions.core.environment.Environment;
import uk.co.magictractor.zillions.core.environment.Priority;
import uk.co.magictractor.zillions.core.environment.StrategyHolder;

/**
 * Creates a very low priority strategy implementation which throws an exception
 * containing useful information when no other strategy implementation can be
 * found.
 */
public class MissingImplStrategyFactory extends AbstractProxyStrategyFactory {

    public MissingImplStrategyFactory() {
        addInterface(Priority.class, () -> {
            return Priority.ERROR_FALLBACK;
        });
    }

    @Override
    Object handle(Class<?> apiClass, Method method, Object[] args) throws Throwable {
        // TODO! could reduce() to a single exception?
        List<Throwable> problems = Environment.getStrategyList(apiClass).strategyHolders().stream().filter(
            (h) -> (h.getUnavailableCause() != null)).map(StrategyHolder::getUnavailableCause).collect(
                Collectors.toList());

        if (problems.size() == 1) {
            throw problems.get(0);
        }
        else if (problems.size() > 1) {
            throw new UnsupportedOperationException(
                "Multiple problems. TODO! enhance code to display more information.");
        }

        // Problem not known.
        throw new UnsupportedOperationException(
            "No implementation found for " + apiClass.getSimpleName() + "  [method=" + method + "]");
    }

}

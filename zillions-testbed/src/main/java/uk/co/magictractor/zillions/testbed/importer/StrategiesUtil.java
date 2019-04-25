package uk.co.magictractor.zillions.testbed.importer;

import java.util.List;

import uk.co.magictractor.zillions.core.environment.Environment;
import uk.co.magictractor.zillions.core.environment.StrategyHolder;

public final class StrategiesUtil {

    private StrategiesUtil() {
    }

    public static <T> void dumpStrategyInfo(Class<T> apiClass) {
        List<StrategyHolder<T>> infos = Environment.getStrategyList(apiClass).strategyHolders();

        for (StrategyHolder<T> info : infos) {
            System.err.println(info.getStrategy() + "  " + info.isAvailable() + "  " + info.getPriority());
        }
    }

}

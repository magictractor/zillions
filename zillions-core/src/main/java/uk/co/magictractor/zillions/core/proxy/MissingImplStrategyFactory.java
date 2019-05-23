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
package uk.co.magictractor.zillions.core.proxy;

import java.lang.reflect.Method;

/**
 * Creates a very low priority strategy implementation which throws an exception
 * containing useful information when no other strategy implementation can be
 * found.
 */
public class MissingImplStrategyFactory extends AbstractProxyStrategyFactory {

    public MissingImplStrategyFactory() {
        //        addInterface(Priority.class, () -> {
        //            return Priority.ERROR_FALLBACK;
        //        });
    }

    @Override
    Object handle(Class<?> apiClass, Method method, Object[] args) throws Throwable {
        //        // TODO! could reduce() to a single exception?
        //        List<Throwable> problems = Environment.getStrategyList(apiClass)
        //                .strategyHolders()
        //                .stream()
        //                .filter((h) -> (h.getUnavailableCause() != null))
        //                .map(StrategyHolder::getUnavailableCause)
        //                .collect(Collectors.toList());
        //
        //        if (problems.size() == 1) {
        //            throw problems.get(0);
        //        }
        //        else if (problems.size() > 1) {
        //            throw new UnsupportedOperationException(
        //                "Multiple problems. TODO! enhance code to display more information.");
        //        }
        //

        // Problem not known.
        throw new UnsupportedOperationException(
            "No implementation found for " + apiClass.getSimpleName() + "  [method=" + method + "]");
    }

}

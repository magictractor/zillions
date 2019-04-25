/**
 * Copyright 2015 Ken Dobson
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
package uk.co.magictractor.zillions.core.environment;

/**
 * <p>
 * * When strategies for any apiClass are discovered, all StrategyFactory
 * implentations are given an opportunity to create an implementation for the
 * API.
 * <p>
 * This is used in zillions-core unit tests to create proxies which allow
 * apiClass implementations to be changed between tests.
 */
public interface StrategyFactory {

    <S> S createInstance(Class<S> apiClass);

}

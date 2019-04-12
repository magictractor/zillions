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
package uk.co.magictractor.zillions.core.api;

import java.util.List;

import uk.co.magictractor.zillions.core.environment.StrategyHolder;

public interface Strategies<S> {

	S bestAvailable();

	List<S> allAvailable();

	/**
	 * Information about all strategy implementations, including those which could
	 * not be loaded, or are explicitly disabled.
	 */
	List<StrategyHolder<S>> strategyHolders();

}

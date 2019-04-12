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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.magictractor.zillions.core.api.Strategies;

public class CachedStrategies<S> implements Strategies<S> {
	private final Logger _logger = LoggerFactory.getLogger(getClass());

	// API class is used only for logging. It cannot be determined at runtime using
	// generics.
	private Class<S> _apiClass;
	private List<StrategyHolder<S>> _strategyHolders = new ArrayList<StrategyHolder<S>>();
	private List<S> _available;
	private S _bestAvailable;

	public CachedStrategies(Class<S> apiClass) {
		_apiClass = apiClass;
	}

	public void addStrategyClass(String strategyClassName, StrategyOption... options) {
		if (strategyClassName == null) {
			throw new IllegalArgumentException("strategyClassName parameter must not be null");
		}
		addStrategyHolder(new StrategyHolder<S>(strategyClassName, options));
	}

	public void addStrategyClass(Class<S> strategyClass, StrategyOption... options) {
		if (strategyClass == null) {
			throw new IllegalArgumentException("strategyClass parameter must not be null");
		}
		addStrategyHolder(new StrategyHolder<S>(strategyClass, options));
	}

	public void addStrategyImplementation(S strategy, StrategyOption... options) {
		if (strategy == null) {
			throw new IllegalArgumentException("strategy parameter must not be null");
		}
		addStrategyHolder(new StrategyHolder<S>(strategy, options));
	}

	public void addStrategies(Strategies<S> strategies) {
		if (strategies == null) {
			throw new IllegalArgumentException("strategies parameter must not be null");
		}

		for (StrategyHolder<S> strategyHolder : strategies.strategyHolders()) {
			addStrategyHolder(strategyHolder);
		}
	}

	public void addStrategyHolder(StrategyHolder<S> strategyHolder) {
		_strategyHolders.add(strategyHolder);
		clearCache();
		_logger.debug("Added {}", strategyHolder);
	}

	public void addStrategyUnavailable(String reason, Throwable cause) {
		addStrategyHolder(new StrategyHolder<>(reason, cause));
	}

	// TODO! should this instead throw an error if _best is not null? - maybe check for marker on best
	private void clearCache() {
		_bestAvailable = null;
		_available = null;
	}

	@Override
	public S bestAvailable() {
		if (_bestAvailable == null) {
			initAvailable();
		}
		return _bestAvailable;
	}

	@Override
	public List<S> allAvailable() {
		if (_available == null) {
			initAvailable();
		}
		return _available;
	}

	private void initAvailable() {
		_available = new ArrayList<>();
		// Assignment here only to satisfy compiler.
		int bestPriority = Integer.MAX_VALUE;
		boolean first = true;
		for (StrategyHolder<S> holder : _strategyHolders) {
			if (holder.isAvailable()) {
				_available.add(holder.getStrategy());
				if (first || holder.getPriority() > bestPriority) {
					// TODO! guard against multiple strategies with the same priority
					bestPriority = holder.getPriority();
					_bestAvailable = holder.getStrategy();
					System.err.println("setting best: " + _bestAvailable);
					first = false;
				}
			}
		}
	}

	@Override
	public List<StrategyHolder<S>> strategyHolders() {
		return _strategyHolders;
	}

}

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

import static org.junit.jupiter.api.DynamicTest.stream;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import uk.co.magictractor.zillions.core.property.PropertyDecorator;
import uk.co.magictractor.zillions.core.property.PropertyStrategy;

// TODO! need interface for this for splitting out zillions-api
public class StrategyHolder<S> {

	// Use the getImplementations to avoid circular dependencies when bootstrapping.
	private static final PropertyDecorator PROPERTIES = new PropertyDecorator(
			Environment.getImplementations().getStrategyListWithoutDiscovery(PropertyStrategy.class));

	private final Logger _logger = LoggerFactory.getLogger(getClass());

	private S _strategy;
	private int _priority = Priority.DEFAULT;
	// TODO! there could be multiple reasons for being unavailable, all should be
	// reported
	private String _unavailableReason;
	private Throwable _unavailableCause;

	// private int _priority;
	// private String _priorityReason;

	public StrategyHolder(S strategy, StrategyOption... options) {
		setStrategy(strategy, options);
	}

	public StrategyHolder(String strategyClassName, StrategyOption... options) {
		// TODO! should call setStrategy to do init() etc
		_strategy = safeConstruct(strategyClassName);
	}

	public StrategyHolder(Class<S> strategyClass, StrategyOption... options) {
		_strategy = safeConstruct(strategyClass);
	}

	public StrategyHolder(String reason, Throwable cause) {
		unavailable(reason, cause);
	}

	private void setStrategy(S strategy, StrategyOption... options) {
		if (strategy instanceof Init) {
			safeInit((Init) strategy);
		}
		if (strategy instanceof Priority) {
			safeSetPriority((Priority) strategy);
		}
		// TODO! marker interface for cannot disable (only SystemPropertyStrategy)?
		// or ask the strategy itself when props is null?
		// or pass in param? (best - flexible for alt. bootstrap strategies)
		if (!hasOption(StrategyOption.SKIP_EXPLICIT_DISABLED_CHECK, options)) {
			checkForExplicitDisable(strategy.getClass());
		}
		_strategy = strategy;
	}

	private boolean hasOption(StrategyOption searchFor, StrategyOption... options) {
		for (StrategyOption candidate : options) {
			if (candidate.equals(searchFor)) {
				return true;
			}
		}
		return false;
	}

	private void checkForExplicitDisable(Class<?> strategyClass) {
		// TODO! null check is a bodge
		if (PROPERTIES == null) {
			System.err.println("PROPERTIES: null?!?!");
			return;
		}

		if (PROPERTIES.getBoolean(strategyClass, "disabled", false)) {
			unavailable("Explicitly disabled by a property", null);
		}
	}

	private S safeConstruct(String strategyClassName) {
		Class<S> strategyClass;
		try {
			strategyClass = (Class<S>) Class.forName(strategyClassName);
		} catch (Throwable e) {
			unavailable("Error loading class", e);
			return null;
		}
		return safeConstruct(strategyClass);
	}

	private S safeConstruct(Class<S> strategyClass) {
		try {
			return constructStrategy(strategyClass);
		} catch (Throwable e) {
			unavailable("Error in constructor", e);
			return null;
		}
	}

	private void safeInit(Init strategy) {
		try {
			strategy.init();
		} catch (Throwable e) {
			unavailable("Error in init()", e);
		}
	}

	private void safeSetPriority(Priority strategy) {
		try {
			_priority = strategy.getPriority();
			System.err.println("*** priority: " + _priority);
		} catch (Throwable e) {
			unavailable("Error in getPriority()", e);
		}
	}

	private S constructStrategy(Class<S> strategyClass) throws Exception {
		Constructor<S> constructor = strategyClass.getConstructor();
		return constructor.newInstance();
	}

	private void unavailable(String reason, Throwable cause) {
		if (reason == null) {
			// TODO! or blank
			throw new IllegalArgumentException("reason parameter must not be null");
		}

		if (cause != null) {
			_logger.error(reason, cause);
		}

		_unavailableReason = reason;
		_unavailableCause = cause;
	}

	public S getStrategy() {
		if (_unavailableReason != null) {
			// TODO! type of exception?
			throw new IllegalStateException("No strategy for reason: " + _unavailableReason, _unavailableCause);
		}
		if (_strategy == null) {
			throw new IllegalStateException("No strategy found, there should have been a reason");
		}
		return _strategy;
	}

	public boolean isAvailable() {
		return _unavailableReason == null;
	}

	public int getPriority() {
		return _priority;
	}

	@Override
	public String toString() {
		ToStringHelper toStringHelper = MoreObjects.toStringHelper(this);
		toStringHelper.add("strategy", _strategy);
		toStringHelper.add("priority", _priority);
		return toStringHelper.toString();
	}

}

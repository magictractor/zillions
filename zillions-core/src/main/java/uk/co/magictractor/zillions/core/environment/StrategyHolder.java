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

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.magictractor.zillions.core.property.PropertyDecorator;
import uk.co.magictractor.zillions.core.property.PropertyStrategy;

public class StrategyHolder<S>
{

  // Use the getImplementations to avoid circular dependencies when bootstrapping.
  private static final PropertyDecorator PROPERTIES = new PropertyDecorator(Environment.getImplementations().getStrategyList(PropertyStrategy.class));

  private final Logger _logger = LoggerFactory.getLogger(getClass());

  private S _strategy;
  private String _unavailableReason;
  private Throwable _unavailableCause;

  // private int _priority;
  // private String _priorityReason;

  public StrategyHolder(S strategy) {
    setStrategy(strategy);
  }

  public StrategyHolder(String strategyClassName) {
    _strategy = safeConstruct(strategyClassName);
  }

  public StrategyHolder(Class<S> strategyClass) {
    _strategy = safeConstruct(strategyClass);
  }

  private void setStrategy(S strategy) {
    if (strategy instanceof Init) {
      safeInit((Init) strategy);
    }
    checkForExplicitDisable(strategy.getClass());
    _strategy = strategy;
  }

  private void checkForExplicitDisable(Class<?> strategyClass) {
    if (PROPERTIES.getBoolean(strategyClass, "disabled", false)) {
      unavailable("Explicitly disabled by a property", null);
    }
  }

  private S safeConstruct(String strategyClassName) {
    Class<S> strategyClass;
    try {
      strategyClass = (Class<S>) Class.forName(strategyClassName);
    } catch (Exception e) {
      unavailable("Error loading class", e);
      return null;
    }
    return safeConstruct(strategyClass);
  }

  private S safeConstruct(Class<S> strategyClass) {
    try {
      return constructStrategy(strategyClass);
    } catch (Exception e) {
      unavailable("Error in constructor", e);
      return null;
    }
  }

  private void safeInit(Init strategy) {
    try {
      strategy.init();
    } catch (Exception e) {
      unavailable("Error in init()", e);
    }
  }

  private S constructStrategy(Class<S> strategyClass)
    throws Exception {
    Constructor<S> constructor = strategyClass.getConstructor();
    return constructor.newInstance();
  }

  private void unavailable(String reason, Exception cause) {
    if (reason == null) {
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

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    stringBuilder.append(_strategy.getClass().getSimpleName());
    if (!isAvailable()) {
      stringBuilder.append(" unavailable because ");
      stringBuilder.append(_unavailableReason);
    }
    stringBuilder.append("}");

    return stringBuilder.toString();
  }

}

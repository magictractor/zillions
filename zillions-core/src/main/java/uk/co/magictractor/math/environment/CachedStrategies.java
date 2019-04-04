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
package uk.co.magictractor.math.environment;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedStrategies<S> implements Strategies<S>
{
  private final Logger _logger = LoggerFactory.getLogger(getClass());

  // API class is used only for logging. It cannot be determined at runtime using
  // generics.
  private Class<S> _apiClass;
  private List<StrategyHolder<S>> _strategyHolders = new ArrayList<StrategyHolder<S>>();
  private List<S> _available;

  public CachedStrategies(Class<S> apiClass) {
    _apiClass = apiClass;
  }

  public void addStrategyClass(String strategyClassName) {
    if (strategyClassName == null) {
      throw new IllegalArgumentException("strategyClassName parameter must not be null");
    }
    addStrategyHolder(new StrategyHolder<S>(strategyClassName));
  }

  public void addStrategyClass(Class<S> strategyClass) {
    if (strategyClass == null) {
      throw new IllegalArgumentException("strategyClass parameter must not be null");
    }
    addStrategyHolder(new StrategyHolder<S>(strategyClass));
  }

  public void addStrategyImplementation(S strategy) {
    if (strategy == null) {
      throw new IllegalArgumentException("strategy parameter must not be null");
    }
    addStrategyHolder(new StrategyHolder<S>(strategy));
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
    _logger.debug("Added {}", strategyHolder);
    clearCache();
    _strategyHolders.add(strategyHolder);
  }

  private void clearCache() {
    _available = null;
  }

  public void addStrategyUnavailable(Throwable cause) {
    if (cause == null) {
      throw new IllegalArgumentException("cause parameter must not be null");
    }
    _logger.error("Strategy is unavailable", cause);
  }

  @Override
  public S firstAvailable() {
    List<S> available = allAvailable();
    if (available.isEmpty()) {
      _logger.error("There are no available implementations of {}", _apiClass);
    }
    return available.isEmpty() ? null : allAvailable().get(0);
  }

  @Override
  public List<S> allAvailable() {
    if (_available == null) {
      _available = findAvailableStrategies();
    }
    return _available;
  }

  private List<S> findAvailableStrategies() {
    List<S> available = new ArrayList<S>();
    for (StrategyHolder<S> description : _strategyHolders) {
      if (description.isAvailable()) {
        available.add(description.getStrategy());
      }
    }
    return available;
  }

  @Override
  public List<StrategyHolder<S>> strategyHolders() {
    return _strategyHolders;
  }

}

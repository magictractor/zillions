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
package uk.co.magictractor.zillions.testbed.strategy;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicNode;
import org.junit.platform.suite.api.SelectClasses;

import uk.co.magictractor.jura.Suite;
import uk.co.magictractor.jura.SuiteStreamBuilder;
import uk.co.magictractor.zillions.testbed.strategy.exporter.BigIntByteExporterTest;
import uk.co.magictractor.zillions.testbed.strategy.importer.BigIntByteImporterTest;

/** Suite which runs the core strategy implementations. */
@SelectClasses({ BigIntByteImporterTest.class, BigIntByteExporterTest.class })
public abstract class StrategySuite {

    @Suite
    public Stream<DynamicNode> suiteFactory() {
        return new SuiteStreamBuilder().selectSuitesInChildPackages().build();
    }

}

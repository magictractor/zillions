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
package uk.co.magictractor.zillions.core;

import uk.co.magictractor.zillions.core.create.CreateStrategy;
import uk.co.magictractor.zillions.core.environment.Environment;

public final class BigIntFactory {

	// TODO! always have a proxy which will be the fallback if nothing else is registered
	private static final CreateStrategy CREATE = Environment.getImplementation(CreateStrategy.class);

	private BigIntFactory() {
	}

	public static BigInt from(String decimal) {
		return CREATE.fromString(decimal);
	}

	public static BigInt from(long value) {
		return CREATE.fromLong(value);
	}

	public static BigInt from(BigInt other) {
		// TODO! check impl type matches?
		// perhaps need to use strategies for cloning? might not always be supported by the BigInt impl.
		return CREATE.copy(other);
	}

}

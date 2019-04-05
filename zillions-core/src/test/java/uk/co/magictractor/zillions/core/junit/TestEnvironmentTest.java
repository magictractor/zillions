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
package uk.co.magictractor.zillions.core.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import uk.co.magictractor.zillions.biginteger.BigIntegerBigInt;
import uk.co.magictractor.zillions.biginteger.BigIntegerCreateStrategy;
import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.core.create.CreateStrategy;
import uk.co.magictractor.zillions.core.environment.Environment;
import uk.co.magictractor.zillions.gmp.GmpJnaBigInt;
import uk.co.magictractor.zillions.gmp.GmpJnaCreateStrategy;

public class TestEnvironmentTest {

	// TODO! ensure it has been registered (so implement before too)
	@RegisterExtension
	private TestContextExtension _textContextRule = new TestContextExtension();

	/**
	 * failing because the first strategy remains in the StrategyListMap, and second
	 * strategy isn't even loaded. want to reset implementations cleanly, but leave
	 * bootstrapped class intact.
	 */
	@Test
	public void t() {

		_textContextRule.addImplementation(new GmpJnaCreateStrategy());
		BigInt i = BigIntFactory.from("1");
		// assertEquals(GmpJnaBigInt.class, i.getClass());
		assertThat(i.getClass()).isEqualTo(GmpJnaBigInt.class);
		TestContext.getInstance().reset();

		_textContextRule.addImplementation(new BigIntegerCreateStrategy());
		BigInt j = BigIntFactory.from("1");
		// assertEquals(BigIntegerBigInt.class, j.getClass());
		assertThat(j.getClass()).isEqualTo(BigIntegerBigInt.class);
	}

	@Test
	public void t2() {

		_textContextRule.addImplementation(new GmpJnaCreateStrategy());
		CreateStrategy strategy = Environment.getImplementation(CreateStrategy.class);
		if (!Proxy.isProxyClass(strategy.getClass())) {
			fail("Strategy implementation should be a proxy for unit tests: " + strategy.getClass().getSimpleName());
		}
	}

}

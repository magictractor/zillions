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
package uk.co.magictractor.zillions.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class TestContextExtensionTest {

    @RegisterExtension
    public TestContextExtension _textContextExtension = new TestContextExtension();

    @Disabled("reinstate test")
    @Test
    public void t() {
        Assertions.fail("reinstate test");

        //        _textContextRule.addImplementation(new NumptyCreateStrategy());
        //        BigInt i = BigIntFactory.from("1");
        //        // assertEquals(GmpJnaBigInt.class, i.getClass());
        //        assertThat(i.getClass()).isEqualTo(NumptyBigInt.class);
        //        // TestContext.getInstance().reset();
        //
        //        _textContextRule.addImplementation(new NoopCreateStrategy());
        //        BigInt j = BigIntFactory.from("1");
        //        // assertEquals(BigIntegerBigInt.class, j.getClass());
        //        assertThat(j.getClass()).isEqualTo(NoopBigInt.class);
    }

    @Disabled("reinstate test")
    @Test
    public void t2() {
        Assertions.fail("reinstate test");

        //        _textContextRule.addImplementation(new NoopCreateStrategy());
        //        CreateStrategy strategy = Environment.findImplementation(CreateStrategy.class);
        //        if (!Proxy.isProxyClass(strategy.getClass())) {
        //            fail("Strategy implementation should be a proxy for unit tests: " + strategy.getClass().getSimpleName());
        //        }
    }

}

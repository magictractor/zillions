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
package uk.co.magictractor.zillions.testbed.dynamic;

import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import com.google.common.collect.Iterables;

import uk.co.magictractor.zillions.testbed.suite.SourceUriSupport;

public class SourceUriSupportTest {

    @Test
    public void testMethodNoArgs() {
        checkMethod("noArgs", "method:/uk/co/magictractor/zillions/testbed/dynamic/SourceUriSupportTest%23noArgs");
    }

    @Test
    public void testMethodSimpleArg() {
        checkMethod("simpleArg",
            "method:/uk/co/magictractor/zillions/testbed/dynamic/SourceUriSupportTest%23simpleArg(boolean)");
    }

    @Test
    public void testMethodSimpleArgs() {
        checkMethod("simpleArgs",
            "method:/uk/co/magictractor/zillions/testbed/dynamic/SourceUriSupportTest%23simpleArgs(java.lang.String,int)");
    }

    @Test
    public void testMethodComplexArgs() {
        checkMethod("complexArgs",
            "method:/uk/co/magictractor/zillions/testbed/dynamic/SourceUriSupportTest%23complexArgs(%5B%5BLjava.lang.String;,%5BI)");
    }

    private void checkMethod(String methodName, String expectedUriString) {
        Method expectedMethod = findMethod(methodName);
        URI uri = SourceUriSupport.fromMethod(expectedMethod);

        Assertions.assertThat(uri.toString()).isEqualTo(expectedUriString);

        //String fqmn = uri.getPath().substring(1).replace('/', '.');
        String[] methodParts = ReflectionUtils.parseFullyQualifiedMethodName(uri.getPath());
        methodParts[0] = methodParts[0].substring(1).replace('/', '.');
        Method actualMethod = DiscoverySelectors.selectMethod(methodParts[0], methodParts[1], methodParts[2])
                .getJavaMethod();
        Assertions.assertThat(actualMethod).isEqualTo(expectedMethod);
    }

    private Method findMethod(String methodName) {
        List<Method> methods = ReflectionSupport.findMethods(SourceUriSupportTest.class,
            m -> methodName.equals(m.getName()), TOP_DOWN);
        return Iterables.getOnlyElement(methods);
    }

    private void noArgs() {
    }

    private void simpleArg(boolean arg) {
    }

    private void simpleArgs(String arg1, int arg2) {
    }

    private void complexArgs(String[][] arg1, int[] arg2) {
    }

}

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
package uk.co.magictractor.zillions.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractDelegatingHandler implements InvocationHandler {

    public abstract Object getDelegate();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object delegate = getDelegate();

        try {
            return method.invoke(delegate, args);
        }
        catch (InvocationTargetException e) {
            // Unwrap and rethrow original exception.
            throw e.getCause();
        }
    }

}

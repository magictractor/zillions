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
package uk.co.magictractor.zillions.core.environment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public final class GenericsHelper {
    private GenericsHelper() {
    }

    public static Class<?> getGenericTypes(Object object) {
        // System.out.println(object.getClass().getTypeParameters()[0]); // S
        // System.out.println(object.getClass().getGenericSuperclass()); //
        // java.util.ArrayList<uk.co.magictractor.math.environment.StrategyDescription<S>>

        // Type[] i = object.getClass().getGenericInterfaces(); //
        // uk.co.magictractor.math.environment.Strategies<S>
        // System.out.println(((ParameterizedType) i[0]));
        // System.out.println(((ParameterizedType)
        // i[0]).getActualTypeArguments()[0].getClass());
        // System.out.println(((ParameterizedType) i[0]).getActualTypeArguments()[0]);// S
        // System.out.println(((ParameterizedType) i[0]).getRawType()); //
        // interface:uk.co.magictractor.math.environment.Strategies
        // System.out.println(((ParameterizedType) i[0]).getOwnerType()); // null
        // return i[0].();

        TypeVariable<?>[] i = object.getClass().getTypeParameters();
        System.out.println(i[0].getGenericDeclaration()); // StrategyList

        Type j = ((ParameterizedType) object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        // System.out.println(j); //
        // uk.co.magictractor.math.environment.StrategyDescription<S>

        return null;
    }

    /*
    // http://tutorials.jenkov.com/java-reflection/generics.html
    public static Class<?> getGenericTypes(Object object, String methodName) throws Exception {
      Method method = object.getClass().getMethod(methodName, new Class[0]);
      ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
      Type[] a = type.getActualTypeArguments();
    }
    */
}

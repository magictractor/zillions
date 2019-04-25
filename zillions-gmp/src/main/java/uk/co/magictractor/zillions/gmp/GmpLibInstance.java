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
package uk.co.magictractor.zillions.gmp;

public final class GmpLibInstance {

    public static GmpLib __lib;

    // private static final boolean IS_AVAILABLE;

    static {
        // TODO! make more robust
        try {
            init();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void init() {
        // One day, but not soon, there might be a JNI implementation.
        // JNA or JNI would be a configuration option.
        __lib = new JnaGmpLib();
    }

    private GmpLibInstance() {
    }

}

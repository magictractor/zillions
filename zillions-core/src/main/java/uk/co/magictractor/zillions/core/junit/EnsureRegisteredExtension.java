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

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * 
 * Generally, setters should not change the environment immediately, unless
 * {@link #isWithinTest()} is true. Instead setters should store state, and the
 * {@link #before} implementation should change the environment. Doing so guards
 * against incorrect use of the Extension (missing annotation, private field
 * etc), causing a test to fail because the environment has not been changed,
 * rather than subsequent tests failing because {@link #after} was not called
 * and the change made for the test remained in the environment.
 */
// TODO! wrong extension used?
// See https://junit.org/junit5/docs/current/user-guide/#extensions-lifecycle-callbacks
// I thought I must have missed something!!
// mayeb don't need this??
public class EnsureRegisteredExtension
		implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback {

	private boolean isRegistered;
	private boolean all;
	private boolean isWithinTest;

	protected boolean isWithinTest() {
		return isWithinTest;
	}

	// TODO! can maybe determine "all" using the context instead.
	// static and non-static use different context impls: MethodExtensionContext and
	// ClassExtensionContext
	@Override
	public final void beforeAll(ExtensionContext context) throws Exception {
		System.err.println("beforeAll");
		all = true;
		isRegistered = true;
		before(context);
	}

	@Override
	public final void beforeEach(ExtensionContext context) throws Exception {
		System.err.println("beforeEach");
		isRegistered = true;
		isWithinTest = true;
		if (!all) {
			before(context);
		}
	}

	protected void ensureRegistered() {
		if (!isRegistered) {
			// TODO! when would this get called if the extension isn't registered??
			throw new IllegalStateException(
					"Extension is not registered, so will be torn down properly, and could cause later tests to fail.");
		}
	}

	@Override
	public final void afterEach(ExtensionContext context) throws Exception {
		System.err.println("afterEach");
		isWithinTest = false;
		if (!all) {
			after(context);
		}
	}

	@Override
	public final void afterAll(ExtensionContext context) throws Exception {
		System.err.println("afterAll");
		after(context);
	}

	public void before(ExtensionContext context) throws Exception {
	}

	public void after(ExtensionContext context) throws Exception {
	}

}

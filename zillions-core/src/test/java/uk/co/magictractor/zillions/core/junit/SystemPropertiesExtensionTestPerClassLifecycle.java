package uk.co.magictractor.zillions.core.junit;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Repeat the extension tests with a PER_CLASS Lifecycle (the other test used
 * the default PER_METHOD Lifecycle).
 */
@TestInstance(Lifecycle.PER_CLASS)
public class SystemPropertiesExtensionTestPerClassLifecycle extends SystemPropertiesExtensionTest {

}
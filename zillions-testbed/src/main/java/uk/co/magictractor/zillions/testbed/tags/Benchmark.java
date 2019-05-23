package uk.co.magictractor.zillions.testbed.tags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Tag;

/**
 * <p>
 * Marker for benchmark unit tests which are excluded from the standard unit
 * test suite.
 * </p>
 * <p>
 * These tests will take time, may take time, and should only be run when no
 * other resource hungry programs are being run.
 * </p>
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Tag("benchmark")
public @interface Benchmark {

}

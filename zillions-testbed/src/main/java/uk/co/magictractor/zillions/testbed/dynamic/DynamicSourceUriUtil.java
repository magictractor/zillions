package uk.co.magictractor.zillions.testbed.dynamic;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.ClasspathResourceSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestIdentifier;

public final class DynamicSourceUriUtil {

    private DynamicSourceUriUtil() {
    }

    public static URI createSourceUri(TestIdentifier testIdentifier) {
        if (!testIdentifier.getSource().isPresent()) {
            // Very unlikely, there should always be a source.
            // Just use the default URI.
            return null;
        }

        String className;
        String methodName = null;
        TestSource source = testIdentifier.getSource().get();
        if (source instanceof MethodSource) {
            // The method name cannot currently be included in the source URI.
            // The best we can do is just link back to the class.
            // See https://github.com/junit-team/junit5/issues/1850
            MethodSource methodSource = (MethodSource) source;
            className = methodSource.getClassName();
            methodName = methodSource.getMethodName();
            // TODO! not how the URL will look see the JUnit issue
        }
        else if (source instanceof ClassSource) {
            className = ((ClassSource) source).getClassName();
        }
        else if (source instanceof ClasspathResourceSource) {
            className = ((ClasspathResourceSource) source).getClasspathResourceName();
            // System.err.println("getClasspathResourceName(): " + className);
        }
        else {
            // TODO! log a warning.
            System.err.println(
                "Code needs modified to create a URI for source type " + source.getClass().getSimpleName());
            return null;
        }

        return createUri(className, methodName);
    }

    private static URI createUri(String className, String methodName) {
        String path = "/" + className.replace('.', '/');
        String query = null;
        if (methodName != null) {
            query = "method=" + methodName;
        }

        return createUri("classpath", path, query);
    }

    private static URI createUri(String scheme, String path, String query) {
        try {
            return new URI(scheme, null, path, query, null);
        }
        catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

}

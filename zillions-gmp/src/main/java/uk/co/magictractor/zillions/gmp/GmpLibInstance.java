package uk.co.magictractor.zillions.gmp;

public class GmpLibInstance {

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

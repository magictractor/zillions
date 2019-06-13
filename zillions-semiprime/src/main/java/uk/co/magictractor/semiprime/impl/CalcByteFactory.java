package uk.co.magictractor.semiprime.impl;

import java.util.ArrayList;
import java.util.List;

import uk.co.magictractor.zillions.environment.BigIntFactory;

public final class CalcByteFactory {

    private CalcByteFactory() {
    }

    public static List<CalcByte> lowBytes(int target) {
        List<CalcByte> lowBytes = new ArrayList<>();

        for (int i = 1; i <= 255; i += 2) {
            for (int j = 1; j <= i; j += 2) {
                if (((i * j) & 0xff) == target) {
                    lowBytes.add(new CalcByte(i, j, BigIntFactory.from((i * j) >> 8)));
                }
            }
        }

        return lowBytes;
    }

    public static List<CalcByte> nextBytes(int loLeft, int loRight, int target) {
        List<CalcByte> lowBytes = new ArrayList<>();

        for (int i = 0; i <= 255; i++) {
            for (int j = 0; j <= 255; j++) {
                int prod = i * loRight + j * loLeft;
                if ((prod & 0xff) == target) {
                    // TODO! roll over previous carry
                    lowBytes.add(new CalcByte(i, j, BigIntFactory.from((prod) >> 8)));
                }
            }
        }

        return lowBytes;
    }

}

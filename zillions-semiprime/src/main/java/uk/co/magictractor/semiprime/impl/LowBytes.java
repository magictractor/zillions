package uk.co.magictractor.semiprime.impl;

import java.util.ArrayList;
import java.util.List;

import uk.co.magictractor.zillions.core.BigIntFactory;

public class LowBytes {

    public static List<CalcByte> lowBytes(int lo) {
        List<CalcByte> lowBytes = new ArrayList<>();

        for (int i = 1; i <= 255; i += 2) {
            for (int j = 1; j <= i; i += 2) {
                if (((i * j) & 0xff) == lo) {
                    // TODO! constant/map for zero??
                    lowBytes.add(new CalcByte(i, j, BigIntFactory.from(0)));
                }
            }
        }

        return lowBytes;
    }
}

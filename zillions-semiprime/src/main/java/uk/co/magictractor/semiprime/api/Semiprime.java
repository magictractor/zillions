package uk.co.magictractor.semiprime.api;

import uk.co.magictractor.semiprime.BigIntPair;
import uk.co.magictractor.zillions.core.BigInt;

public interface Semiprime {

    /** Calculate the tow prime factors of a given semiprime number.
     *
     * If the parameter is not a semiprime implementations may
     * throw an exception or return any pair of factors which multiply together to give the parameter.
     *
     * @param semiprime a product of two prime numbers, so smallest allowed value is 4 (2*2)
     * @return the two prime numbers which are factors of the semiprime
     */
    BigIntPair calculateFactors(BigInt semiprime);

}

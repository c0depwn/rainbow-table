package ch.fhnw.kry;

import java.math.BigInteger;

public class Reduction {
    public static char[] pwSymbols;
    public static int pwLength;

    public static String reduce(byte[] hash, int depth) {
        // interpret hash as natural number
        var h = new BigInteger(1, hash).add(BigInteger.valueOf(depth));
        char[] result = new char[pwLength];

        for (int i = 1; i <= pwLength; i++) {
            // calculate div and mod
            var dm = h.divideAndRemainder(BigInteger.valueOf(pwSymbols.length));
            result[pwLength - i] =  pwSymbols[dm[1].intValue()];

            // set div as new h
            h = dm[0];
        }
        return new String(result);

    }
}

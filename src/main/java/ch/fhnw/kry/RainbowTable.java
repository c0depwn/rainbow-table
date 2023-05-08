package ch.fhnw.kry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Consumer;

public class RainbowTable {
    private static final int CHAIN_SIZE = 2000;
    private static final int PW_MAX = 2000;
    private static final char[] symbols = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z'};
    private final Map<String, Chain> chainsByEnd = new HashMap<>();

    public void initialize() {
        Reduction.pwSymbols = symbols;
        Reduction.pwLength = 7;

        char[] password = {'0', '0', '0', '0', '0', '0', '0'};
        generate(PW_MAX, 0, 0, password, symbols, this::prepareChain);
    }

    public String find(String hashVal) throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("MD5");

        // start from end of chain 1999, 1998, ...
        for (int start = CHAIN_SIZE - 1; start >= 0; start--) {
            // get hash bytes
            var hash = HexFormat.of().parseHex(hashVal);
            var reduced = "";

            // reduce and hash from the start
            for (int round = start; round < CHAIN_SIZE; round++) {
                reduced = Reduction.reduce(hash, round);
                hash = md.digest(reduced.getBytes());
            }

            // no match try new start
            if (!chainsByEnd.containsKey(reduced)) continue;

            // found, find pw
            var c = chainsByEnd.get(reduced);
            var tempReduced = c.getStart();
            var tempHash = new byte[]{};
            for (int i = 0; i < start; i++) {
                tempHash = md.digest(tempReduced.getBytes());
                tempReduced = Reduction.reduce(tempHash, i);
            }

            var original = HexFormat.of().parseHex(hashVal);
            if (!Arrays.equals(original, md.digest(tempReduced.getBytes()))) continue;
            return tempReduced;
        }

        return "NOT FOUND";
    }

    private void prepareChain(String password) {
        try {
            var c = new Chain(password, CHAIN_SIZE);
            c.initialize();
            chainsByEnd.put(c.getEnd(), c);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private int generate(int max, int count, int prefixEndIdx, char[] pw, char[] possibleChars, Consumer<String> next) {
        if (prefixEndIdx == pw.length) {
            next.accept(new String(pw));
            return count + 1;
        }

        for (char possibleChar : possibleChars) {
            pw[prefixEndIdx] = possibleChar;
            count = generate(max, count, prefixEndIdx + 1, pw, possibleChars, next);
            if (count > max) return count;
        }
        return count;
    }
}

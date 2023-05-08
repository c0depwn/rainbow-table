package ch.fhnw.kry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;

public class Chain {
    private String start;
    private String end;
    private int size;

    public Chain(String start, int size) {
        this.start = start;
        this.size = size;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public void initialize() throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("MD5");

        String value = start;
        for (int i = 0; i < size; i++) {
            value = Reduction.reduce(md.digest(value.getBytes()), i);
        }
        end = value;
    }

}

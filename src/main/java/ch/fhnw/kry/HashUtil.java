package ch.fhnw.kry;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    public static String hash(String val) throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("MD5");
        var hashBytes = md.digest(val.getBytes());
        var temp = new BigInteger(1, hashBytes);
        return temp.toString(16);
    }
}

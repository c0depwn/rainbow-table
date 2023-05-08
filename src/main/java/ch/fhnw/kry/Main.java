package ch.fhnw.kry;

import java.security.NoSuchAlgorithmException;

public class Main {
    private static String hash = "1d56a37fb6b08aa709fe90e12ca59e12";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        var table = new RainbowTable();
        table.initialize();

        var result = table.find(hash);
        System.out.printf("the password is: %s\n", result);
    }
}
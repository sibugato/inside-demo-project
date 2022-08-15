package com.sibgatullinvv.insidedemoproject.utils;

/*
Простой механизм кодирования и декодирования, чтобы хранить пароли в БД в закодированном виде.
Взят первый попавшийся в свободном доступе, подробного описания не будет, ибо впоследствии будет заменён на что-то приличное
 */

public class EncodeUtil {

    public static String key = "CrowDecoder4";

    public static String encrypt(String sourceString) {
        char [] p = key.toCharArray ();
        int n = p.length;
        char[] c = sourceString.toCharArray();
        int m = c.length;
        for (int k = 0; k < m; k++) {
            int mima = c [k] + p [k / n];
            c[k] = (char) mima;
        }
        return new String(c);
    }

    public static String decrypt(String sourceString) {
        char [] p = key.toCharArray ();
        int n = p.length;
        char[] c = sourceString.toCharArray();
        int m = c.length;
        for (int k = 0; k < m; k++) {
            int mima = c [k] -p [k / n];
            c[k] = (char) mima;
        }
        return new String(c);
    }
}
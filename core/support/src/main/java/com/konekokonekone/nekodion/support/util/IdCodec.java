package com.konekokonekone.nekodion.support.util;

import org.springframework.stereotype.Component;

@Component
public class IdCodec {

    private static final String ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String encode(long id) {
        StringBuilder sb = new StringBuilder();

        while (id > 0) {
            int r = (int)(id % 62);
            sb.append(ALPHABET.charAt(r));
            id /= 62;
        }

        return sb.reverse().toString();
    }

    public long decode(String code) {

        long result = 0;

        for (char c : code.toCharArray()) {
            result = result * 62 + ALPHABET.indexOf(c);
        }

        return result;
    }
}

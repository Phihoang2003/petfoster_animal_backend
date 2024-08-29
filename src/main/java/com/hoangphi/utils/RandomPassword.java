package com.hoangphi.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomPassword {
    public String randomPassword() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(chars[rnd.nextInt(chars.length)]);
        }
        return sb.toString();
    }
}

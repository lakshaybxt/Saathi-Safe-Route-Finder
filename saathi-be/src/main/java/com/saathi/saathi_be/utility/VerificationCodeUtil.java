package com.saathi.saathi_be.utility;

import java.util.Random;

public class VerificationCodeUtil {

    public static String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}

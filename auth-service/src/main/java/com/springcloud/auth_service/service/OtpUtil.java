package com.springcloud.auth_service.service;

import java.security.SecureRandom;
import java.util.Random;

public class OtpUtil {
    public static String generateOtp()
    {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }
}

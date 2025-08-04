/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.utils;

import java.security.SecureRandom;

/**
 *
 * @author PC
 */
public class MyUserUtil {
    public static String generateSecureOTP() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }
    
    public static String getUserCode(String email){
        return email.substring(0,10);
    }
    
    public static boolean isOuEmail(String email) {
        if (email == null) return false;
        return true;
//        return email.matches("^[A-Za-z0-9._%+-]+@ou\\.edu\\.vn$");
    }
    
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpper = true;
            } else if (Character.isLowerCase(ch)) {
                hasLower = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if ("!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~".indexOf(ch) >= 0) {
                hasSpecial = true;
            }
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}

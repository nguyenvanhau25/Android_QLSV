package com.example.qlsv_kthp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Các tiện ích bảo mật (Mã hóa mật khẩu)
 */
public class SecurityUtils {
    
    /**
     * Mã hóa chuỗi văn bản bằng thuật toán SHA-256
     */
    public static String sha256(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Chuyển mảng byte sang chuỗi Hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(0xFF & b);
                while (h.length() < 2) h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

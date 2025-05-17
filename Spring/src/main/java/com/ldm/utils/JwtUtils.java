package com.ldm.utils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author huu-thanhduong
 */
public class JwtUtils {

    // SECRET nên được lưu bằng biến môi trường,
    private static final String SECRET = "12345678901234567890123456789012"; // 32 ký tự (AES key)
    private static final long EXPIRATION_MS = 86400000; // 1 ngày

    public static String generateToken(Integer id, String username, String role) throws Exception {
        JWSSigner signer = new MACSigner(SECRET);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username) // Subject là username
                .claim("id", id) // Thêm id người dùng vào token
                .claim("role", role) // Thêm role vào token
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_MS)) // Thời gian hết hạn
                .issueTime(new Date()) // Thời gian phát hành
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public static String validateTokenAndGetUsername(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET);

        if (signedJWT.verify(verifier)) {
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiration.after(new Date())) {
                return signedJWT.getJWTClaimsSet().getSubject();
            }
        }
        return null;
    }

    public static Map<String, Object> validateTokenAndGetUserDetails(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET);

        // Kiểm tra tính hợp lệ của token
        if (signedJWT.verify(verifier)) {
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

            // Kiểm tra token có còn hiệu lực không
            if (expiration.after(new Date())) {
                String username = signedJWT.getJWTClaimsSet().getSubject();
                String role = (String) signedJWT.getJWTClaimsSet().getClaim("role"); // Lấy role
                Long id = signedJWT.getJWTClaimsSet().getLongClaim("id"); // Lấy id

                if (username != null && role != null && id != null) {
                    // Trả về Map chứa thông tin id, username, role
                    Map<String, Object> userDetails = new HashMap<>();
                    userDetails.put("id", id);
                    userDetails.put("username", username);
                    userDetails.put("role", role);

                    return userDetails;
                }
            }
        }

        // Nếu token không hợp lệ hoặc hết hạn
        return null;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ldm.pojo.User;
import com.ldm.services.UserService;
import com.ldm.utils.JwtUtils;
import com.ldm.utils.MyUserUtil;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class ApiGoogleController {

    @Autowired
    private UserService userDetailsService;

    private static final String WEB_CLIENT_ID = "680589489153-d932o1cnulr5juc3ff04ar2l6eck5ep4.apps.googleusercontent.com";

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) throws GeneralSecurityException, IOException, Exception {
        String idTokenString = body.get("idToken");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(WEB_CLIENT_ID)) // Nhớ đúng Client ID
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            if(!MyUserUtil.isOuEmail(email)){
                throw new IllegalArgumentException("Bạn phải dùng email trường");
            }
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String avatar = (String) payload.get("picture");

            // Tìm user
            User user = userDetailsService.getUserByEmail(email);
            if (user == null) {
                // Nếu chưa có, tạo mới
                user = new User();
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setAvatar(avatar);
                user.setUserCode(MyUserUtil.getUserCode(email));
                user.setPassword(null);
                user = userDetailsService.addStudent(user);
            }
            
            String token = JwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole());

//            String jwt = Jwts.builder()
//                    .setSubject(email)
//                    .claim("name", name)
//                    .setExpiration(new Date(System.currentTimeMillis() + 86400000))
//                    .signWith(SignatureAlgorithm.HS256, "your-secret-key")
//                    .compact();

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } else {
            throw new IllegalArgumentException("chưa có tokenid");
        }

    }

}

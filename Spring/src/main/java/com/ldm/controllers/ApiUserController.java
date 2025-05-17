/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.pojo.User;
import com.ldm.services.UserService;
import com.ldm.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api/")
@CrossOrigin
public class ApiUserController {

    @Autowired
    private UserService userDetailsService;

//    @PostMapping(path = "users/",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> create(@RequestParam Map<String, String> params, @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
//        return new ResponseEntity<>(this.userDetailsService.addUser(params, avatar), HttpStatus.CREATED);
//    }

    @PostMapping("login/")
    public ResponseEntity<?> login(@RequestBody User u) {

        if (this.userDetailsService.authenticate(u.getEmail(), u.getPassword())) {
            try {
                User userInfo = this.userDetailsService.getUserByEmail(u.getEmail());
                String token = JwtUtils.generateToken(userInfo.getId(), userInfo.getEmail(), userInfo.getRole());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }
    
    @GetMapping("secure/userInfo/")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        // Lấy thông tin từ request attributes (được set từ filter)
        Long id = (Long) request.getAttribute("id");
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (id == null || username == null || role == null) {
            // Trả về lỗi nếu thông tin không hợp lệ
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "User information is not available. Please check the token."));
        }

        // Trả về thông tin người dùng dưới dạng Map
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", id);
        userInfo.put("username", username);
        userInfo.put("role", role);

        return ResponseEntity.ok(userInfo);  // Trả về thông tin người dùng
    }

    @RequestMapping("secure/profile/")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<User> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userDetailsService.getUserByEmail(principal.getName()), HttpStatus.OK);
    }

    @RequestMapping("secure/roles/")
@ResponseBody
@CrossOrigin
public ResponseEntity<Map<String, Object>> getRoles(Principal principal) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Lấy danh sách các quyền của người dùng (vai trò)
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    
    // Tạo một map để trả về
    Map<String, Object> response = new HashMap<>();
    response.put("username", principal.getName());
    response.put("roles", authorities.stream()
                                      .map(GrantedAuthority::getAuthority)
                                      .collect(Collectors.toList()));

    return new ResponseEntity<>(response, HttpStatus.OK);
}

    @RequestMapping("secure/authorities/")
@ResponseBody
@CrossOrigin
public ResponseEntity<Map<String, Object>> getAuthorities(Principal principal) {
    // Lấy thông tin xác thực từ SecurityContext
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Lấy danh sách các authorities (quyền của người dùng)
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

    // Tạo một map để trả về
    Map<String, Object> response = new HashMap<>();
    response.put("username", principal.getName());
    response.put("authorities", authorities.stream()
                                            .map(GrantedAuthority::getAuthority)
                                            .collect(Collectors.toList()));

    return new ResponseEntity<>(response, HttpStatus.OK);
}

}

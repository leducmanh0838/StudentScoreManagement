/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.pojo.PreStudentRegistration;
import com.ldm.services.PreStudentRegistrationService;
import com.ldm.services.UserService;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api/")
@CrossOrigin
public class ApiPreStudentRegistration {
    @Autowired
    private UserService userDetailsService;
    @Autowired
    private PreStudentRegistrationService preStudentService;
    
    @PostMapping(path="preStudent/register/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<?> registerPreStudent(@RequestParam Map<String, String> params, @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
    try {
        PreStudentRegistration registered = preStudentService.register(params, avatar);
        System.out.println("avatar: " +avatar);
        return ResponseEntity.ok("Đăng ký thành công. Vui lòng kiểm tra email để xác thực.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Đăng ký thất bại: " + e.getMessage());
    }

//        try {
//        preStudentService.register(preStudent.getEmail(), preStudent.getPassword());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
//
//        return new ResponseEntity<>("Đăng ký thành công. Vui lòng kiểm tra email để xác thực.", headers, HttpStatus.OK);
//    } catch (Exception e) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
//
//        return new ResponseEntity<>("Đăng ký thất bại: " + e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}

}

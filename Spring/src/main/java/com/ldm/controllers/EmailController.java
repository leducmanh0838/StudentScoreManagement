package com.ldm.controllers;

import com.ldm.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/email")
    public ResponseEntity<String> sendTestEmail() {
        emailService.sendSimpleEmail("2251012090manh@ou.edu.vn", "Chao ban", "Hello from Spring!");
        return ResponseEntity.ok("Email sent!");
    }
}
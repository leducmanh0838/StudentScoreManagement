/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.services.EnrollmentService;
import com.ldm.services.impl.EnrollmentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiEnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;
    
    @PostMapping("/secure/enrollment/register")
    public ResponseEntity<?> registerCourseSession(HttpServletRequest request, @RequestBody Map<String, String> requestBody){
        try{
            Integer userId = ((Number) request.getAttribute("id")).intValue();
            if(enrollmentService.register(userId, Integer.parseInt(requestBody.get("courseSessionId"))))
                return ResponseEntity.ok("Đăng ký thành công!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể thêm đăng ký.");
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + ex.getMessage());
        } 
    }
    
    @GetMapping("secure/enrollment/getEnrollments")
    public ResponseEntity<?> getEnrollments(HttpServletRequest request){
        Integer userId = ((Number) request.getAttribute("id")).intValue();
        
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(userId));
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.dto.ScoreAndCriteriaDTO;
import com.ldm.services.EnrollmentService;
import com.ldm.services.GradeService;
import com.ldm.services.impl.EnrollmentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    @Autowired
    private GradeService gradeService;
    
    @PostMapping("/secure/studentAuth/enrollment/register")
    public ResponseEntity<?> registerCourseSession(HttpServletRequest request, @RequestBody Map<String, String> requestBody){
        Integer userId = ((Number) request.getAttribute("id")).intValue();
        if(enrollmentService.register(userId, Integer.parseInt(requestBody.get("courseSessionId"))))
            return ResponseEntity.ok("Đăng ký thành công!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đăng ký thất bại!");
    }
    
    @GetMapping("secure/studentAuth/enrollment/getEnrollments")
    public ResponseEntity<?> getEnrollments(HttpServletRequest request){
        Integer userId = ((Number) request.getAttribute("id")).intValue();
        
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(userId));
    }
    
    @GetMapping("secure/studentAuth/enrollment/{enrollmentId}/getGrades")
    public ResponseEntity<List<ScoreAndCriteriaDTO>> getGrades(@PathVariable("enrollmentId") int enrollmentId, HttpServletRequest request) {
        Integer userId = ((Number) request.getAttribute("id")).intValue();
        if(!enrollmentService.isStudentOwnerOfEnrollment(enrollmentId, userId)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không phải là sinh viên đăng ký môn này!"
            );
        }
        
        List<ScoreAndCriteriaDTO> grades = gradeService.getGradesByEnrollmentId(enrollmentId);
        return ResponseEntity.ok(grades);
    }
}

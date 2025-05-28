/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.dto.GradeReponseDTO;
import com.ldm.dto.GradeRequestDTO;
import com.ldm.dto.UpdateGradeRequestDTO;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.Grade;
import com.ldm.services.CourseSessionService;
import com.ldm.services.GradeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiGradeController {
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private CourseSessionService courseSessionService;

    @PostMapping("/secure/teacherAuth/grade/addGrades")
    public ResponseEntity<?> addGrades(@RequestBody GradeRequestDTO input, HttpServletRequest request) {
        Integer teacherId = ((Number) request.getAttribute("id")).intValue();
        if(!courseSessionService.isTeacherOwnerOfCourseSession(input.getCourseSessionId(), teacherId)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không phải là giảng viên môn này!"
            );
        }
        
        if(courseSessionService.getGradeStatusByCourseSessionId(input.getCourseSessionId()).equals(CourseSession.LOCKED)){
            throw new IllegalArgumentException("Điểm đã khóa!");
        }
        
        List<Grade> grades = gradeService.addGrades(input);

        List<GradeReponseDTO> dtoList = grades.stream()
            .map(GradeReponseDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
    
    @PostMapping("/secure/teacherAuth/grade/updateGrades")
    public ResponseEntity<?> updateGrades(@RequestBody UpdateGradeRequestDTO req, HttpServletRequest request) {
        Integer teacherId = ((Number) request.getAttribute("id")).intValue();
        if(!courseSessionService.isTeacherOwnerOfCourseSession(req.getCourseSessionId(), teacherId)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không phải là giảng viên môn này!"
            );
        }
        
        if(courseSessionService.getGradeStatusByCourseSessionId(req.getCourseSessionId()).equals(CourseSession.LOCKED)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Điểm đã khóa!"
            );
        }
        
        boolean success = gradeService.updateGrades(req);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Cập nhật thất bại");
    }
    
    
}

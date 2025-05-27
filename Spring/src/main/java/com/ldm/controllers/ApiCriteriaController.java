/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.dto.CriteriaInCourseSessionDTO;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.Criteria;
import com.ldm.services.CourseSessionService;
import com.ldm.services.CriteriaService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class ApiCriteriaController {
    @Autowired
    private CriteriaService criteriaService;
    
    @Autowired
    private CourseSessionService courseSessionService;
    
    @GetMapping("/secure/courseSession/{courseSessionId}/getCriterias")
    public ResponseEntity<List<CriteriaInCourseSessionDTO>> getCriteriasByCourseSession(
            @PathVariable(name="courseSessionId") Integer courseSessionId) {

        List<Criteria> criteriaList = criteriaService.getCriteriaByCourseSession(courseSessionId);
        List<CriteriaInCourseSessionDTO> dtoList = criteriaList.stream()
                .map(CriteriaInCourseSessionDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
    
    @PostMapping("/secure/teacherAuth/addCriterias/{courseSessionId}")
    public ResponseEntity<?> addCriteriasByCourseSession(
            @PathVariable(name="courseSessionId") Integer courseSessionId,
            @RequestBody List<Criteria> criteriaRequestList,
            HttpServletRequest request) {
        // kiểm tra sở hữu
        Integer teacherId = ((Number) request.getAttribute("id")).intValue();
        if(!courseSessionService.isTeacherOwnerOfCourseSession(courseSessionId, teacherId)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không phải là giảng viên môn này!"
            );
        }
        
        List<Criteria> criteriaReponseList = criteriaService.addList(criteriaRequestList, courseSessionId);
        
        List<Map<String, String>> result = criteriaReponseList.stream().map(s -> {
                Map<String, String> m = new HashMap<>();
//                m.put("id", s.getId().toString());
                m.put("criteriaName", s.getCriteriaName());
//                m.put("type", s.getType());
                m.put("courseSessionId", s.getCourseSessionId().getId().toString());
                return m;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(result);
    }
    
    @PostMapping("/secure/teacherAuth/addCriteria/{courseSessionId}")
    public ResponseEntity<?> addCriteriaByCourseSession(
            @PathVariable(name="courseSessionId") Integer courseSessionId,
            @RequestBody Criteria criteriaRequest) {
        CourseSession courseSession = new CourseSession(courseSessionId);
        criteriaRequest.setCourseSessionId(courseSession);
        criteriaService.addOrUpdate(criteriaRequest);
        
        return ResponseEntity.ok("Thêm thành công!");
    }
}
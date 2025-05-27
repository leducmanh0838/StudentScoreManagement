/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.services.CourseService;
import com.ldm.services.CourseSessionService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCourseController {
    @Autowired
    private CourseService courseService;
    
    @GetMapping("/courses/getAllNames")
    public ResponseEntity<?> getCourseSessions() {
        List<Object[]> results = courseService.getAllCourseNames();

        // Chuyển đổi Object[] thành danh sách Map để dễ đọc ở frontend
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", row[0]);
            item.put("name", row[1]);
            response.add(item);
        }

        return ResponseEntity.ok(response);
    }
    
}

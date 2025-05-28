/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.dto.GradeInfoDTO;
import com.ldm.dto.StudentGradeMailDTO;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.Criteria;
import com.ldm.repositories.impl.CourseRepositoryImpl;
import com.ldm.services.CourseService;
import com.ldm.services.CourseSessionService;
import com.ldm.services.GradeService;
import com.ldm.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
public class ApiCourseSessionController {

    @Autowired
    private CourseSessionService courseSessionService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private UserService userService;

    @GetMapping("/courseSessions")
    public ResponseEntity<List<Map<String, Object>>> getCourseSessions(@RequestParam Map<String, String> params) {
        List<Object[]> results = courseSessionService.getCourseSessions(params);

        // Chuyển đổi Object[] thành danh sách Map để dễ đọc ở frontend
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", row[0]);
            item.put("code", row[1]);
            item.put("courseName", row[2]);
            item.put("teacherName", row[3]);
            item.put("isOpen", row[4]);
            response.add(item);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/secure/teacherAuth/courseSessions")
    public ResponseEntity<?> getCourseSessionsByTeacher(HttpServletRequest request) {
        Integer teacherId = ((Number) request.getAttribute("id")).intValue();
        return ResponseEntity.ok(courseSessionService.getCourseSessionsByTeacherId(teacherId));
    }

    @GetMapping("/secure/teacherAuth/courseSession/{courseSessionId}/getGradeStatus")
    public ResponseEntity<?> getGradeStatus(@PathVariable(name = "courseSessionId") Integer courseSessionId, HttpServletRequest request) {
        Integer teacherId = ((Number) request.getAttribute("id")).intValue();
        if (!courseSessionService.isTeacherOwnerOfCourseSession(courseSessionId, teacherId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không phải là giảng viên môn này!"
            );
        }
        //courseSessionService.getGradeStatusByCourseSessionId(courseSessionId)

        return ResponseEntity.ok(courseSessionService.getGradeStatusByCourseSessionId(courseSessionId));
    }

    @PostMapping("/secure/teacherAuth/courseSession/{courseSessionId}/lock")
    public ResponseEntity<?> lockGrade(@PathVariable(name = "courseSessionId") Integer courseSessionId, HttpServletRequest request) {
        Integer teacherId = ((Number) request.getAttribute("id")).intValue();
        if (!courseSessionService.isTeacherOwnerOfCourseSession(courseSessionId, teacherId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không phải là giảng viên môn này!"
            );
        }

        if (courseSessionService.getGradeStatusByCourseSessionId(courseSessionId).equals(CourseSession.LOCKED)) {
            throw new IllegalArgumentException("Bạn đã khóa điểm rồi!");
        }
        List<StudentGradeMailDTO> studentGrades = gradeService.getStudentGradeMailByCourseSessionId(courseSessionId);

        if (courseSessionService.lockGradeStatus(courseSessionId)) //            return ResponseEntity.ok("Khóa điểm thành công!");
        {
            return ResponseEntity.ok(studentGrades);
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Khóa điểm thất bại!");
        }
    }

    @GetMapping("/secure/teacherAuth/courseSession/{courseSessionId}/getGrades")
    public ResponseEntity<?> getGradeByCourseSession(
            @PathVariable(name = "courseSessionId") Integer courseSessionId,
            HttpServletRequest request) {
        Integer teacherId = ((Number) request.getAttribute("id")).intValue();
        if (!courseSessionService.isTeacherOwnerOfCourseSession(courseSessionId, teacherId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không phải là giảng viên môn này!"
            );
        }

        return ResponseEntity.ok(gradeService.getGradesByCourseSessionId(courseSessionId));
    }
}

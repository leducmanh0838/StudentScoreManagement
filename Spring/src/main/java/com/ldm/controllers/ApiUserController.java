/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.dto.StudentInCourseDTO;
import com.ldm.pojo.User;
import com.ldm.services.CourseSessionService;
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
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {

    @Autowired
    private UserService userDetailsService;
    @Autowired
    private CourseSessionService courseSessionService;

//    @PostMapping(path = "users/",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> create(@RequestParam Map<String, String> params, @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
//        return new ResponseEntity<>(this.userDetailsService.addUser(params, avatar), HttpStatus.CREATED);
//    }

    @PostMapping("/login")
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
    
    @GetMapping("/secure/teacherAuth/getStudentsByCourseSession")
    public ResponseEntity<List<StudentInCourseDTO>> getStudentsByCourseSession(@RequestParam Map<String, String> params, HttpServletRequest request) {
        Integer teacherId = ((Number) request.getAttribute("id")).intValue();
        Integer courseSessionId = Integer.parseInt(params.get("courseSessionId"));
        if(!courseSessionService.isTeacherOwnerOfCourseSession(courseSessionId, teacherId)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không phải là giảng viên môn này!"
            );
        }
        
        List<StudentInCourseDTO> students = userDetailsService.getStudentsInCourseSession(params);

        return ResponseEntity.ok(students);
    }
}

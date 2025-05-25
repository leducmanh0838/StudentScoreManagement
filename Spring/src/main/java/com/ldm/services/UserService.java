/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services;

import com.ldm.dto.StudentInCourseDTO;
import com.ldm.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PC
 */
public interface UserService extends UserDetailsService{
//public interface UserService{
    List<User> getAllUsers();
    List<Object[]> getAllTeacherNames();
    List<User> getUsersForStaff(Map<String, String> params);
    User addOrUpdateTeacher(User teacher);
    User getUserById(int id);
//    User addUser(Map<String, String> params, MultipartFile avatar);
    User getUserByEmail(String email);
    boolean authenticate(String email, String password);
    User addStudent(User student);
    List<StudentInCourseDTO> getStudentsInCourseSession(Map<String, String> params);
}

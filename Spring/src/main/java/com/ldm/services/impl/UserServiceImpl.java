/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ldm.pojo.User;
import com.ldm.repositories.UserRepository;
import com.ldm.repositories.impl.UserRepositoryImpl;
import com.ldm.services.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PC
 */
@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.getAllUsers();
    }

    @Override
    public List<User> getUsersForStaff(Map<String, String> params) {
        return this.userRepository.getUsers(params);
    }

    @Override
    public User addOrUpdateTeacher(User teacher) {

        if (!teacher.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(teacher.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                teacher.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (teacher.getPassword() != null && !teacher.getPassword().isEmpty()) {
            String hashed = passwordEncoder.encode(teacher.getPassword());
            teacher.setPassword(hashed);
        }
        teacher.setRole(User.TEACHER_ROLE);
        System.out.println("role " + teacher.getId());
        return this.userRepository.addOrUpdateUser(teacher);
    }

    @Override
    public User getUserById(int id) {
        return this.userRepository.getUserById(id);
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = this.getUserByEmail(email);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(), authorities);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.getUserByEmail(email);
    }

    @Override
    public boolean authenticate(String email, String password) {
        return this.userRepository.authenticate(email, password);
    }

    @Override
    public List<Object[]> getAllTeacherNames() {
        return this.userRepository.getAllTeacherNames();
    }

    @Override
    public User addStudent(User student) {
        student.setRole(User.STUDENT_ROLE);
        return this.userRepository.addOrUpdateUser(student);
    }

    @Override
    public List<User> getStudentsInCourseSession(Map<String, String> params) {
        String courseSessionIdStr = params.get("courseSessionId");
        if (courseSessionIdStr == null || courseSessionIdStr.isEmpty()) {
            throw new IllegalArgumentException("Thiếu mã buổi học");
        }

        Integer courseSessionId;
        try {
            courseSessionId = Integer.parseInt(courseSessionIdStr);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Mã buổi học không hợp lệ, phải là số nguyên", ex);
        }

        return userRepository.getStudentsInCourseSession(params);
    }
}

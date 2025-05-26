/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.dto.EnrollmentInfoForStudentDTO;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.Enrollment;
import com.ldm.pojo.User;
import com.ldm.repositories.CourseSessionRepository;
import com.ldm.repositories.EnrollmentRepository;
import com.ldm.repositories.UserRepository;
import com.ldm.services.EnrollmentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class EnrollmentServiceImpl implements EnrollmentService{
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseSessionRepository courseSessionRepository;
    
    @Override
    public boolean register(Integer studentId, Integer courseSessionId) {
        Enrollment enrollment = new Enrollment();
        User student = userRepository.getUserById(studentId);
        if (student==null) {
            throw new IllegalArgumentException("không tìm thấy user");
        }
        if (!student.getRole().equals(User.STUDENT_ROLE)) {
            throw new IllegalArgumentException("bạn không phải là sinh viên");
        }
        
        CourseSession courseSession = courseSessionRepository.getById(courseSessionId);
        if (!courseSession.getIsOpen()) {
            throw new IllegalArgumentException("Buổi học đã đóng, không đăng ký được");
        }
        
        if(enrollmentRepository.existsByUserIdAndCourseSessionId(studentId, courseSessionId)){
            throw new IllegalArgumentException("Đã đăng ký rồi!!!");
        }
        
        enrollment.setUserId(student);
        enrollment.setCourseSessionId(courseSession);
        
        return enrollmentRepository.addOrUpdate(enrollment)!=null?true:false;
    }

    @Override
    public boolean addMidAndFinalScore(Float midScore, Float finalScore) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<EnrollmentInfoForStudentDTO> getEnrollmentsByStudentId(int userId) {
        return this.enrollmentRepository.getEnrollmentsByStudentId(userId);
    }

    @Override
    public boolean isStudentOwnerOfEnrollment(int enrollmentId, int studentId) {
        return this.enrollmentRepository.isStudentOwnerOfEnrollment(enrollmentId, studentId);
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.dto.EnrollmentGradeDTO;
import com.ldm.dto.GradeInfoDTO;
import com.ldm.dto.GradeRequestDTO;
import com.ldm.dto.ScoreAndCriteriaDTO;
import com.ldm.dto.ScoreDTO;
import com.ldm.dto.ScoreUpdateDTO;
import com.ldm.dto.StudentGradeMailDTO;
import com.ldm.dto.UpdateGradeRequestDTO;
import com.ldm.pojo.Grade;
import com.ldm.repositories.CourseRepository;
import com.ldm.repositories.CriteriaRepository;
import com.ldm.repositories.GradeRepository;
import com.ldm.services.EmailService;
import com.ldm.services.GradeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private EmailService emailService;

    @Override
    public List<Grade> addGrades(GradeRequestDTO req) {
        // 1. courseSessionId phải là số nguyên dương
        if (req.getCourseSessionId() <= 0) {
            throw new IllegalArgumentException("courseSessionId phải là số nguyên dương");
        }

        List<EnrollmentGradeDTO> enrollments = req.getEnrollments();

        // 2. enrollments không được null hoặc rỗng
        if (enrollments == null || enrollments.isEmpty()) {
            throw new IllegalArgumentException("Danh sách enrollments không được để trống");
        }

        for (EnrollmentGradeDTO enrollment : enrollments) {
            // 3. enrollmentId phải hợp lệ
            if (enrollment.getEnrollmentId() <= 0) {
                throw new IllegalArgumentException("enrollmentId phải là số nguyên dương");
            }

            List<ScoreDTO> scores = enrollment.getScores();

            // 4. scores không được null hoặc rỗng
            if (scores == null || scores.isEmpty()) {
                throw new IllegalArgumentException("enrollmentId " + enrollment.getEnrollmentId() + " không có scores");
            }

            for (ScoreDTO scoreDTO : scores) {
                int criteriaId = scoreDTO.getCriteriaId();
                float score = scoreDTO.getScore();

                // 5. criteriaId phải hợp lệ
                if (criteriaId <= 0) {
                    throw new IllegalArgumentException("criteriaId phải là số nguyên dương");
                }

                // 6. score phải từ 0 đến 10
                if (score < 0 || score > 10) {
                    throw new IllegalArgumentException("Điểm phải từ 0 đến 10 (score = " + score + ")");
                }
            }
        }

        // Nếu mọi thứ hợp lệ thì tiếp tục gọi repository để thêm dữ liệu
        return gradeRepository.addGrades(req);
    }

    @Override
    public boolean updateGrades(UpdateGradeRequestDTO req) {
        // 1. courseSessionId phải là số nguyên dương
        if (req.getCourseSessionId() <= 0) {
            throw new IllegalArgumentException("courseSessionId phải là số nguyên dương");
        }

        List<ScoreUpdateDTO> scores = req.getScores();

        // 2. scores không được null hoặc rỗng
        if (scores == null || scores.isEmpty()) {
            throw new IllegalArgumentException("Danh sách scores không được để trống");
        }

        for (ScoreUpdateDTO scoreUpdate : scores) {
            int gradeId = scoreUpdate.getGradeId();
            float score = scoreUpdate.getScore();

            // 3. gradeId phải hợp lệ
            if (gradeId <= 0) {
                throw new IllegalArgumentException("gradeId phải là số nguyên dương");
            }

            // 4. score phải nằm trong khoảng 0 đến 10
            if (score < 0 || score > 10) {
                throw new IllegalArgumentException("Điểm phải từ 0 đến 10 (score = " + score + ")");
            }
        }

        // Nếu hợp lệ thì gọi repository xử lý cập nhật
        return gradeRepository.updateGrades(req);
    }

    @Override
    public List<ScoreAndCriteriaDTO> getGradesByEnrollmentId(int enrollmentId) {
        return this.gradeRepository.getGradesByEnrollmentId(enrollmentId);
    }

    @Override
    public List<GradeInfoDTO> getGradesByCourseSessionId(int courseSessionId) {
        return this.gradeRepository.getGradesByCourseSessionId(courseSessionId);
    }

    @Override
    public List<StudentGradeMailDTO> getStudentGradeMailByCourseSessionId(int courseSessionId) {
        List<StudentGradeMailDTO> students = this.gradeRepository.getStudentGradeMailByCourseSessionId(courseSessionId);
        String courseName= courseRepository.getCourseNameByCourseSessionId(courseSessionId);
        emailService.sendGradesToStudents(students, courseName);
        return students;
    }

}

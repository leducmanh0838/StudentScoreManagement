/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.dto.EnrollmentGradeDTO;
import com.ldm.repositories.*;
import com.ldm.dto.GradeRequestDTO;
import com.ldm.dto.ScoreDTO;
import com.ldm.dto.ScoreUpdateDTO;
import com.ldm.dto.UpdateGradeRequestDTO;
import com.ldm.pojo.Criteria;
import com.ldm.pojo.Enrollment;
import com.ldm.pojo.Grade;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author PC
 */
@Repository
@Transactional
public class GradeRepositoryImpl implements GradeRepository {

    private final int BATCH_SIZE = 20;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Override
    public List<Grade> addGrades(GradeRequestDTO req) {
        Session session = factory.getObject().getCurrentSession();

        int courseSessionId = req.getCourseSessionId();
        List<EnrollmentGradeDTO> enrollments = req.getEnrollments();

        if (enrollments == null || enrollments.isEmpty()) {
            return Collections.emptyList(); // hoặc trả về null tùy ý
        }

        // Lấy danh sách enrollmentId từ request
        List<Integer> enrollmentIds = enrollments.stream()
                .map(EnrollmentGradeDTO::getEnrollmentId)
                .collect(Collectors.toList());

        // Kiểm tra enrollment có nằm trong course session
        if (!this.enrollmentRepository.areAllEnrollmentsInCourseSession(enrollmentIds, courseSessionId)) {
            throw new IllegalArgumentException("Có enrollment không thuộc course session");
        }

        // Lấy danh sách Criteria từ courseSession
        List<Criteria> criteriaList = this.criteriaRepository.getCriteriaByCourseSesion(courseSessionId);
        Set<Integer> validCriteriaIdSet = criteriaList.stream()
                .map(Criteria::getId)
                .collect(Collectors.toSet());

        List<Grade> addedGrades = new ArrayList<>();
        int batchSize = BATCH_SIZE;
        int counter = 0;

        for (EnrollmentGradeDTO enrollment : enrollments) {
            int enrollmentId = enrollment.getEnrollmentId();

            for (ScoreDTO scoreDTO : enrollment.getScores()) {
                if (!validCriteriaIdSet.contains(scoreDTO.getCriteriaId())) {
                    throw new IllegalArgumentException("Criteria ID " + scoreDTO.getCriteriaId()
                            + " không thuộc course session " + courseSessionId);
                }

                Grade grade = new Grade();
                grade.setEnrollmentId(new Enrollment(enrollmentId));
                grade.setCriteriaId(new Criteria(scoreDTO.getCriteriaId()));
                grade.setScore(scoreDTO.getScore());

                session.persist(grade);
                addedGrades.add(grade);

                if (++counter % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
            }
        }

        return addedGrades;
    }

    @Override
    public boolean updateGrades(UpdateGradeRequestDTO req) {
        Session session = factory.getObject().getCurrentSession();
        int courseSessionId = req.getCourseSessionId();
        List<ScoreUpdateDTO> scores = req.getScores();

        // Lấy danh sách gradeIds từ request
        List<Integer> gradeIds = scores.stream()
                .map(ScoreUpdateDTO::getGradeId)
                .collect(Collectors.toList());

        // Kiểm tra tất cả gradeId đều thuộc courseSession
        if (!this.areAllGradesInCourseSession(gradeIds, courseSessionId)) {
            throw new IllegalArgumentException("grade này không thuộc buổi học !!");
        }

        for (ScoreUpdateDTO scoreDTO : scores) {
            Integer gradeId = scoreDTO.getGradeId();
            Float newScore = scoreDTO.getScore();

            String hql = "UPDATE Grade g SET g.score = :score WHERE g.id = :gradeId";
            int updated = session.createQuery(hql)
                    .setParameter("score", newScore)
                    .setParameter("gradeId", gradeId)
                    .executeUpdate();

            if (updated == 0) {
                throw new IllegalArgumentException("Grade ID " + gradeId + " không tồn tại");
            }

        }
        return true;
    }

    @Override
    public boolean areAllGradesInCourseSession(List<Integer> gradeIds, int courseSessionId) {
        Session session = factory.getObject().getCurrentSession();

        Long matchedCount = session.createQuery(
                "SELECT COUNT(g.id) FROM Grade g "
                + "WHERE g.id IN (:gradeIds) "
                + "AND g.enrollmentId.courseSessionId.id = :courseSessionId", Long.class)
                .setParameter("gradeIds", gradeIds)
                .setParameter("courseSessionId", courseSessionId)
                .getSingleResult();

        return matchedCount == gradeIds.size();
    }
}

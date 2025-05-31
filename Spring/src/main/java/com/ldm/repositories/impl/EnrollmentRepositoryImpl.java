/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.configs.LoggerConfig;
import com.ldm.dto.EnrollmentInfoForStudentDTO;
import com.ldm.pojo.Enrollment;
import com.ldm.repositories.EnrollmentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Enrollment addOrUpdate(Enrollment enrollment) {
        Session s = this.factory.getObject().getCurrentSession();
        if (enrollment.getId() == null) {
            s.persist(enrollment);
        } else {
            s.merge(enrollment);
        }
        return enrollment;
    }

    @Override
    public boolean existsByUserIdAndCourseSessionId(int userId, int courseSessionId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "SELECT COUNT(e.id) FROM Enrollment e WHERE e.userId.id = :userId AND e.courseSessionId.id = :courseSessionId";
        Long count = session.createQuery(hql, Long.class)
                .setParameter("userId", userId)
                .setParameter("courseSessionId", courseSessionId)
                .uniqueResult();

        return count != null && count > 0;
    }

//    public boolean areAllStudentsEnrolledInCourseSession(List<Integer> studentIds, Integer courseSessionId) {
//        LoggerConfig.info("areAllStudentsEnrolledInCourseSession");
//        Session session = this.factory.getObject().getCurrentSession();
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
//        Root<Enrollment> root = cq.from(Enrollment.class);
//
//        // WHERE course_session_id = :csId AND user_id IN (:studentIds)
//        Predicate csPredicate = cb.equal(root.get("courseSessionId").get("id"), courseSessionId);
//        Predicate studentPredicate = root.get("userId").get("id").in(studentIds);
//
//        cq.select(cb.count(root)).where(cb.and(csPredicate, studentPredicate));
//
//        Long count = session.createQuery(cq)
//                .setParameter("csId", courseSessionId)
//                .getSingleResult();
//
//        return count == studentIds.size(); // tất cả đều tồn tại
//    }
    @Override
    public boolean areAllEnrollmentsInCourseSession(List<Integer> enrollmentIds, int courseSessionId) {
        if (enrollmentIds == null || enrollmentIds.isEmpty()) {
            return false;
        }

        Session session = factory.getObject().getCurrentSession();

        Long count = session.createQuery(
                "SELECT COUNT(e.id) FROM Enrollment e "
                + "WHERE e.id IN (:ids) AND e.courseSessionId.id = :courseSessionId", Long.class)
                .setParameter("ids", enrollmentIds)
                .setParameter("courseSessionId", courseSessionId)
                .getSingleResult();

        return count == enrollmentIds.size();
    }

    @Override
    public List<EnrollmentInfoForStudentDTO> getEnrollmentsByStudentId(int userId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = """
        SELECT new EnrollmentInfoForStudentDTO(e.id, c.name, e.createdDate, cs.id)
        FROM Enrollment e
        JOIN e.courseSessionId cs
        JOIN cs.courseId c
        WHERE e.userId.id = :userId
        ORDER BY e.createdDate DESC
        """;

        Query<EnrollmentInfoForStudentDTO> query = session.createQuery(hql, EnrollmentInfoForStudentDTO.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Override
    public boolean isStudentOwnerOfEnrollment(int enrollmentId, int studentId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(e) FROM Enrollment e WHERE e.id = :enrollmentId AND e.userId.id = :studentId";
        Long count = s.createQuery(hql, Long.class)
                .setParameter("enrollmentId", enrollmentId)
                .setParameter("studentId", studentId)
                .uniqueResult();
        return count != null && count > 0;
    }
    
    @Override
    public String getGradeStatusByEnrollmentId(int enrollmentId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "SELECT cs.gradeStatus "
                   + "FROM Enrollment e "
                   + "JOIN e.courseSessionId cs "
                   + "WHERE e.id = :enrollmentId";

        return session.createQuery(hql, String.class)
                      .setParameter("enrollmentId", enrollmentId)
                      .uniqueResult();
    }
}

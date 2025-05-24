/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.configs.LoggerConfig;
import com.ldm.pojo.Enrollment;
import com.ldm.repositories.EnrollmentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
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

    public boolean areAllStudentsEnrolledInCourseSession(List<Integer> studentIds, Integer courseSessionId) {
        LoggerConfig.info("areAllStudentsEnrolledInCourseSession");
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Enrollment> root = cq.from(Enrollment.class);

        // WHERE course_session_id = :csId AND user_id IN (:studentIds)
        Predicate csPredicate = cb.equal(root.get("courseSessionId").get("id"), courseSessionId);
        Predicate studentPredicate = root.get("userId").get("id").in(studentIds);

        cq.select(cb.count(root)).where(cb.and(csPredicate, studentPredicate));

        Long count = session.createQuery(cq)
                .setParameter("csId", courseSessionId)
                .getSingleResult();

        return count == studentIds.size(); // tất cả đều tồn tại
    }
//@Override
//    public CourseSession addOrUpdate(CourseSession cs) {
//        Session s = this.factory.getObject().getCurrentSession();
//        if(cs.getId()==null){
//            s.persist(cs);
//        }
//        else{
//            s.merge(cs);
//        }
//        
//        return cs;
//    }    

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.pojo.Course;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.User;
import com.ldm.repositories.CourseSessionRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
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
public class CourseSessionRepositoryImpl implements CourseSessionRepository {

    public static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public CourseSession addOrUpdate(CourseSession cs) {
        Session s = this.factory.getObject().getCurrentSession();
        if (cs.getId() == null) {
            s.persist(cs);
        } else {
            s.merge(cs);
        }

        return cs;
    }

    @Override
    public List<Object[]> getCourseSessions(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        // Root CourseSession
        Root<CourseSession> root = cq.from(CourseSession.class);

        // Join Course and User (teacher)
        Join<CourseSession, Course> courseJoin = root.join("courseId", JoinType.INNER);
        Join<CourseSession, User> teacherJoin = root.join("teacherId", JoinType.INNER);

        // Multiselect: choose fields to select
        cq.multiselect(
                root.get("id"),
                root.get("code"),
                courseJoin.get("name"), // Course name
                cb.concat(cb.concat(teacherJoin.get("firstName"), " "), teacherJoin.get("lastName")), // Teacher's name
                root.get("isOpen") // Is open
        );

        // Apply filtering for courseId if provided
        if (params.containsKey("courseId")) {
            Long courseId = Long.valueOf(params.get("courseId"));
            cq.where(cb.equal(courseJoin.get("id"), courseId));
        }

        // Create query and set pagination
        Query<Object[]> query = s.createQuery(cq);

        // Pagination: Set start position (page - 1) * PAGE_SIZE and max results
        int page = Integer.parseInt(params.getOrDefault("page", "1")); // Default to page 1
        query.setFirstResult((page - 1) * PAGE_SIZE); // Start from the correct offset (page - 1) * PAGE_SIZE
        query.setMaxResults(PAGE_SIZE); // Limit results per page

        return query.getResultList();
    }

    @Override
    public boolean registerCourseSession() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CourseSession getById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(CourseSession.class, id); // Dùng Hibernate để lấy theo primary key
    }

    @Override
    public boolean isTeacherOwnerOfCourseSession(Integer courseSessionId, Integer teacherId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> query = session.createQuery(
                "SELECT COUNT(cs.id) FROM CourseSession cs WHERE cs.id = :csId AND cs.teacherId.id = :teacherId", Long.class
        );
        query.setParameter("csId", courseSessionId);
        query.setParameter("teacherId", teacherId);
        return query.getSingleResult() > 0;
    }

    @Override
    public String getGradeStatusByCourseSessionId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT cs.gradeStatus FROM CourseSession cs WHERE cs.id = :id";
        return session.createQuery(hql, String.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public boolean lockGradeStatus(Integer courseSessionId) {
        Session session = factory.getObject().getCurrentSession();

        String hql = "UPDATE CourseSession cs SET cs.gradeStatus = :lockedStatus "
                + "WHERE cs.id = :id AND cs.gradeStatus = :currentStatus";

        int updated = session.createQuery(hql)
                .setParameter("lockedStatus", CourseSession.LOCKED)
                .setParameter("currentStatus", CourseSession.DRAFT)
                .setParameter("id", courseSessionId)
                .executeUpdate();

        return updated > 0;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.finalexamhibernate.HibernateUtils;
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
public class CourseSessionRepositoryImpl implements CourseSessionRepository {

    public static final int PAGE_SIZE = 6;

    @Override
    public CourseSession addOrUpdate(CourseSession cs) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            if (cs.getId() == null) {
                s.persist(cs);
            } else {
                s.merge(cs);
            }

            return cs;
        }
    }

    @Override
    public List<Object[]> getCourseSessions(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

            // Root CourseSession
            Root<CourseSession> root = cq.from(CourseSession.class);

            // Join Course and User (teacher)
            Join<CourseSession, Course> courseJoin = root.join("courseId", JoinType.INNER);
            Join<CourseSession, User> teacherJoin = root.join("teacherId", JoinType.INNER);

            // Multiselect: choose fields to select
            cq.multiselect(
                    courseJoin.get("name"), // Course name
                    cb.concat(cb.concat(teacherJoin.get("firstName"), " "), teacherJoin.get("lastName")), // Teacher's name
                    root.get("maxSlots") // Max slots
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
    }

    @Override
    public boolean registerCourseSession() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

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
public class CourseSessionRepositoryImpl implements CourseSessionRepository{
    public static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public CourseSession addOrUpdate(CourseSession cs) {
        Session s = this.factory.getObject().getCurrentSession();
        if(cs.getId()==null){
            s.persist(cs);
        }
        else{
            s.merge(cs);
        }
        
        return cs;
    }

    @Override
    public List<Object[]> getCourseSessions(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<CourseSession> root = cq.from(CourseSession.class);
        Join<CourseSession, Course> courseJoin = root.join("courseId", JoinType.INNER);
        Join<CourseSession, User> teacherJoin = root.join("teacherId", JoinType.INNER);

        cq.multiselect(
            courseJoin.get("name"),
            cb.concat(cb.concat(teacherJoin.get("firstName"), " "), teacherJoin.get("lastName")),
            root.get("maxSlots")
        );

        Query<Object[]> query = s.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public boolean registerCourseSession() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

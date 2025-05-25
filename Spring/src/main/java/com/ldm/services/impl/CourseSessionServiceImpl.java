/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.repositories.impl.*;
import com.ldm.pojo.Course;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.User;
import com.ldm.repositories.CourseRepository;
import com.ldm.repositories.CourseSessionRepository;
import com.ldm.services.CourseSessionService;
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
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class CourseSessionServiceImpl implements CourseSessionService{
    @Autowired
    private CourseSessionRepository repo;
    
    @Override
    public CourseSession addOrUpdate(CourseSession cs) {
        return this.repo.addOrUpdate(cs);
    }

    @Override
    public List<Object[]> getCourseSessions(Map<String, String> params) {
        return this.repo.getCourseSessions(params);
    }

    @Override
    public boolean registerCourseSession() {
        return this.repo.registerCourseSession();
    }

    @Override
    public CourseSession getById(int id) {
        return this.repo.getById(id);
    }
    
    @Override
    public boolean isTeacherOwnerOfCourseSession(Integer courseSessionId, Integer teacherId){
        return repo.isTeacherOwnerOfCourseSession(courseSessionId, teacherId);
    }

    @Override
    public String getGradeStatusByCourseSessionId(int id) {
        return repo.getGradeStatusByCourseSessionId(id);
    }

    @Override
    public boolean lockGradeStatus(Integer courseSessionId) {
        return repo.lockGradeStatus(courseSessionId);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.dto.CourseSessionListForTeacherDTO;
import com.ldm.pojo.CourseSession;
import com.ldm.repositories.CourseSessionRepository;
import com.ldm.services.CourseSessionService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<CourseSessionListForTeacherDTO> getCourseSessionsByTeacherId(int teacherId) {
        return this.repo.getCourseSessionsByTeacherId(teacherId);
    }

    @Override
    public boolean isStudentEnrolledInCourseSession(Integer courseSessionId, Integer studentId) {
        return this.repo.isStudentEnrolledInCourseSession(courseSessionId, studentId);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services;

import com.ldm.dto.CourseSessionListForTeacherDTO;
import com.ldm.pojo.CourseSession;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC
 */
public interface CourseSessionService {
    
    CourseSession addOrUpdate(CourseSession cs);
    CourseSession getById(int id);
    List<Object[]> getCourseSessions(Map<String, String> params);
    boolean registerCourseSession();
    boolean isTeacherOwnerOfCourseSession(Integer courseSessionId, Integer teacherId);
    String getGradeStatusByCourseSessionId(int id);
    boolean lockGradeStatus(Integer courseSessionId);
    List<CourseSessionListForTeacherDTO> getCourseSessionsByTeacherId(int teacherId);
    boolean isStudentEnrolledInCourseSession(Integer courseSessionId, Integer studentId);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories;

import com.ldm.pojo.CourseSession;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC
 */
public interface CourseSessionRepository {
    
    CourseSession addOrUpdate(CourseSession cs);
//    SELECT 
//    c.name AS courseName,
//    CONCAT(u.first_name, ' ', u.last_name) AS teacherFullName,
//    cs.max_slots AS maxSlots
//FROM Course_Session cs
//JOIN Course c ON cs.course_id = c.id
//JOIN User u ON cs.teacher_id = u.id;
    List<Object[]> getCourseSessions(Map<String, String> params);
    boolean registerCourseSession();
}

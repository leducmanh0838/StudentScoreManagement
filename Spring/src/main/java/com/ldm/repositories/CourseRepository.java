/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories;

import com.ldm.pojo.Course;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC
 */
public interface CourseRepository {
    Course addOrUpdate(Course course);
    List<Course> getCourses(Map<String, String> params);
    List<Object[]> getAllCourseNames();
}

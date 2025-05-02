/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.pojo.Course;
import com.ldm.repositories.CourseRepository;
import com.ldm.services.CourseService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class CourseServiceImpl implements CourseService{
    @Autowired
    private CourseRepository courseRepository;
    
    @Override
    public Course addOrUpdate(Course course) {
        return courseRepository.addOrUpdate(course);
    }

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        return courseRepository.getCourses(params);
    }
    
}

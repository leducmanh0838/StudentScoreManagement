/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.formatters;

import com.ldm.pojo.Course;
import com.ldm.pojo.User;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author PC
 */
public class CourseFormatter implements Formatter<Course>{

    @Override
    public String print(Course object, Locale locale) {
        return String.valueOf(object.getId());
    }

    @Override
    public Course parse(String text, Locale locale) throws ParseException {
        Course c = new Course();
        c.setId(Integer.valueOf(text));
        return c;
    }

}

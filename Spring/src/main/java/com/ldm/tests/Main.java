/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.tests;

import com.ldm.configs.DispatcherServletInit;
import com.ldm.pojo.Course;
import com.ldm.repositories.impl.CourseRepositoryImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author PC
 */
public class Main {
    public static void main(String[] args) {
        // Tạo context từ class cấu hình Spring (Config class của bạn, ví dụ AppConfig.class)
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DispatcherServletInit.class);

        // Lấy Bean từ context
        CourseRepositoryImpl courseRepositoryImpl = ctx.getBean(CourseRepositoryImpl.class);

        // Tạo tham số tìm kiếm
        Map<String, String> params = new HashMap<>();
        params.put("name", "abc");
        params.put("page", "1");

        // Gọi hàm để test
        List<Course> courses = courseRepositoryImpl.getCourses(params);

        // In kết quả
        for (Course p : courses) {
            System.out.printf("ID: %d - Name: %s\n", p.getId(), p.getName());
        }

        ctx.close();
    }
}

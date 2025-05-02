/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.ldm.finalexamhibernate;

import com.ldm.repositories.impl.CourseRepositoryImpl;
import com.ldm.repositories.impl.CourseSessionRepositoryImpl;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author PC
 */
public class FinalExamHibernate {

    public static void main(String[] args) {

        // Tạo tham số tìm kiếm
        Map<String, String> params = new HashMap<>();
//        params.put("name", "Data");
//        System.out.println("getCourses");
        CourseSessionRepositoryImpl s = new CourseSessionRepositoryImpl();
        s.getCourseSessions(params).forEach(c->{
            System.out.printf("%s: %s %d\n", c[0], c[1], c[2]);
        });
    }
}

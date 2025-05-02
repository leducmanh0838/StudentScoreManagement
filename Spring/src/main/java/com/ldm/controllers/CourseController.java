/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.pojo.Course;
import com.ldm.pojo.User;
import com.ldm.repositories.impl.CourseRepositoryImpl;
import com.ldm.services.CourseService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author PC
 */
@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;
    
    @GetMapping("/courseListView/")
    public String userListView(Model model, @RequestParam Map<String, String> params) {
        
        List<Course> courses = this.courseService.getCourses(params);
        model.addAttribute("courses", courses);
        model.addAttribute("isLoadMore", courses.size()==CourseRepositoryImpl.PAGE_SIZE);
    //        model.addAttribute("debugMessage", "Số lượng users trả về: " + users.size());
        
        if(params.get("page")!=null)
            model.addAttribute("currentPage", Integer.valueOf(params.get("page")));
        else
            model.addAttribute("currentPage", 1);
        return "courseListView";
    }
    
    @PostMapping("/addOrUpdateCourse/")
    public String addOrUpdateCourse(@RequestParam("name") String name) {
        Course c = new Course();
        c.setName(name);
        this.courseService.addOrUpdate(c);
        return "redirect:/courseListView/";
    }
}

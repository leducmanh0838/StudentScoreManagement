/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.pojo.Course;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.User;
import com.ldm.repositories.impl.CourseRepositoryImpl;
import com.ldm.services.CourseService;
import com.ldm.services.CourseSessionService;
import com.ldm.services.UserService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author PC
 */
@Controller
public class CourseSessionController {
    @Autowired
    private CourseSessionService service;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    
    @GetMapping("/courseSessionListView/")
    public String userListView(Model model, @RequestParam Map<String, String> params) {
        
        List<Object[]> courseSessions = this.service.getCourseSessions(params);
        model.addAttribute("courseSessions", courseSessions);
        model.addAttribute("isLoadMore", courseSessions.size()==CourseRepositoryImpl.PAGE_SIZE);
        
        if(params.get("page")!=null)
            model.addAttribute("currentPage", Integer.valueOf(params.get("page")));
        else
            model.addAttribute("currentPage", 1);
        return "courseSessionListView";
    }
    
    @GetMapping("/addOrUpdateCourseSessionView/")
    public String addCourseSessionView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("courseSession", new CourseSession());
        model.addAttribute("courses", courseService.getAllCourseNames());
        model.addAttribute("teachers", userService.getAllTeacherNames());
        return "addOrUpdateCourseSession";
    }
    
    @GetMapping("/addOrUpdateCourseSessionView/{course_session_id}/")
    public String updateCourseSessionView(Model model, @PathVariable(value="course_session_id") int id) {
        model.addAttribute("courseSession", this.service.getById(id));
        model.addAttribute("courses", courseService.getAllCourseNames());
        model.addAttribute("teachers", userService.getAllTeacherNames());
        return "addOrUpdateCourseSession";
    }
    
    @PostMapping("/addOrUpdateCourseSession/")
    public String addOrUpdateCourseSession(@ModelAttribute(value = "courseSession") CourseSession courseSession){
        this.service.addOrUpdate(courseSession);
        return "redirect:/courseSessionListView/";
    }
}

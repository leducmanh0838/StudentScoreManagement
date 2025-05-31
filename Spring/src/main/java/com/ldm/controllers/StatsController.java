/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.configs.LoggerConfig;
import com.ldm.dto.CourseSessionStatsDTO;
import com.ldm.services.CourseService;
import com.ldm.services.StatsService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author PC
 */
@Controller
public class StatsController {

    @Autowired
    StatsService statsService;
    @Autowired
    CourseService courseService;

    @GetMapping("/stats")
    public String stats(Model model, @RequestParam(name = "courseId", required = false) Integer courseId) {
        List<Object[]> courseNames = courseService.getAllCourseNames();
        model.addAttribute("courseNames", courseNames);
        model.addAttribute("selectedCourseId", courseId); // để giữ lại selection
        
//        List<String> labels = Arrays.asList("TH001-IT1000", "TH002-IT1000", "TH003-IT1000", "TH004-IT1000");
//        // Dữ liệu số lượng sinh viên - List<Integer>
//        List<Integer> data = Arrays.asList(4, 8, 8, 8);
//
//        model.addAttribute("labels", labels);
//        model.addAttribute("data", data);

        if (courseId != null) {
            List<CourseSessionStatsDTO> stats = statsService.countEnrollmentsByCourse(courseId);
            for (CourseSessionStatsDTO s : stats) {
                LoggerConfig.info("s.getEnrollmentCount() {}", s.getEnrollmentCount());
            }
            model.addAttribute("stats", stats);
        }

        return "stats";
    }
}

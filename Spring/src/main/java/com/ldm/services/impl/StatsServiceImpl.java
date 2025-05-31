/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.dto.CourseSessionStatsDTO;
import com.ldm.dto.GradeStatsDTO;
import com.ldm.repositories.StatsRepository;
import com.ldm.services.StatsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class StatsServiceImpl implements StatsService{

    @Autowired
    private StatsRepository statsRepository;

    @Override
    public List<CourseSessionStatsDTO> countEnrollmentsByCourse(int courseId, Integer year) {
        return this.statsRepository.countEnrollmentsByCourse(courseId, year);
    }
    
    @Override
    public List<GradeStatsDTO> studentPerformanceStats(int courseId, Integer year) {
        return this.statsRepository.studentPerformanceStats(courseId, year);
    }
    
}

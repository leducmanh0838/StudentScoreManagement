/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories;

import com.ldm.dto.CourseSessionStatsDTO;
import com.ldm.dto.GradeStatsDTO;
import java.util.List;

/**
 *
 * @author PC
 */
public interface StatsRepository {
    List<CourseSessionStatsDTO> countEnrollmentsByCourse(int courseId, Integer year);
    List<GradeStatsDTO> studentPerformanceStats(int courseId, Integer year);
}

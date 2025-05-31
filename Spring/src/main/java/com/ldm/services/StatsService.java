/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services;

import com.ldm.repositories.*;
import com.ldm.dto.CourseSessionStatsDTO;
import java.util.List;

/**
 *
 * @author PC
 */
public interface StatsService {
    List<CourseSessionStatsDTO> countEnrollmentsByCourse(int courseId);
}

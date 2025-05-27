/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services;

import com.ldm.dto.EnrollmentInfoForStudentDTO;
import java.util.List;

/**
 *
 * @author PC
 */
public interface EnrollmentService {
    boolean register(Integer studentId, Integer courseSessionId);
    boolean addMidAndFinalScore(Float midScore, Float finalScore);
    List<EnrollmentInfoForStudentDTO> getEnrollmentsByStudentId(int userId);
    boolean isStudentOwnerOfEnrollment(int enrollmentId, int studentId);
    String getGradeStatusByEnrollmentId(int enrollmentId);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services;


import com.ldm.dto.GradeInfoDTO;
import com.ldm.repositories.*;
import com.ldm.dto.GradeRequestDTO;
import com.ldm.dto.ScoreAndCriteriaDTO;
import com.ldm.dto.StudentGradeMailDTO;
import com.ldm.dto.UpdateGradeRequestDTO;
import com.ldm.pojo.Grade;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC
 */
public interface GradeService {
    List<Grade> addGrades(GradeRequestDTO req);
    boolean updateGrades(UpdateGradeRequestDTO req);
    List<ScoreAndCriteriaDTO> getGradesByEnrollmentId(int enrollmentId);
    List<GradeInfoDTO> getGradesByCourseSessionId(int courseSessionId);
    List<StudentGradeMailDTO> getStudentGradeMailByCourseSessionId(int courseSessionId);
}

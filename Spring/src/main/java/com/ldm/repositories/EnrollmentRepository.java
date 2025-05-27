/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories;

import com.ldm.dto.EnrollmentInfoForStudentDTO;
import com.ldm.pojo.Enrollment;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC
 */
public interface EnrollmentRepository {
    Enrollment addOrUpdate(Enrollment enrollment);
    boolean existsByUserIdAndCourseSessionId(int userId, int courseSessionId);
    boolean areAllEnrollmentsInCourseSession(List<Integer> enrollmentIds, int courseSessionId);
    List<EnrollmentInfoForStudentDTO> getEnrollmentsByStudentId(int userId);
    boolean isStudentOwnerOfEnrollment(int enrollmentId, int studentId);
    String getGradeStatusByEnrollmentId(int enrollmentId);
}

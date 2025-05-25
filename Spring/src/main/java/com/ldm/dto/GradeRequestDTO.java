package com.ldm.dto;

import java.util.List;

public class GradeRequestDTO {
    private int courseSessionId;
    private List<EnrollmentGradeDTO> enrollments;

    public GradeRequestDTO(int courseSessionId, List<EnrollmentGradeDTO> enrollments) {
        this.courseSessionId = courseSessionId;
        this.enrollments = enrollments;
    }

    public GradeRequestDTO() {
    }

    
    /**
     * @return the courseSessionId
     */
    public int getCourseSessionId() {
        return courseSessionId;
    }

    /**
     * @param courseSessionId the courseSessionId to set
     */
    public void setCourseSessionId(int courseSessionId) {
        this.courseSessionId = courseSessionId;
    }

    /**
     * @return the enrollments
     */
    public List<EnrollmentGradeDTO> getEnrollments() {
        return enrollments;
    }

    /**
     * @param enrollments the enrollments to set
     */
    public void setEnrollments(List<EnrollmentGradeDTO> enrollments) {
        this.enrollments = enrollments;
    }
    
    
}
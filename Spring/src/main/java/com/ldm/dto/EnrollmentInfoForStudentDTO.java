package com.ldm.dto;

import java.util.Date;

public class EnrollmentInfoForStudentDTO {
    private int enrollmentId;
    private String courseName;
    private Date enrollmentDate;
    private int courseSessionId;

    public EnrollmentInfoForStudentDTO(int enrollmentId, String courseName, Date enrollmentDate, int courseSessionId) {
        this.enrollmentId = enrollmentId;
        this.courseName = courseName;
        this.enrollmentDate = enrollmentDate;
        this.courseSessionId=courseSessionId;
    }

    /**
     * @return the enrollmentId
     */
    
    public int getEnrollmentId() {
        return enrollmentId;
    }

    /**
     * @param enrollmentId the enrollmentId to set
     */
    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    /**
     * @return the courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * @param courseName the courseName to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * @return the enrollmentDate
     */
    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    /**
     * @param enrollmentDate the enrollmentDate to set
     */
    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
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

    
}
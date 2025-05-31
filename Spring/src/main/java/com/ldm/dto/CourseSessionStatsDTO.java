package com.ldm.dto;
public class CourseSessionStatsDTO {
    private int courseSessionId;
    private String courseSessionCode;
    private long enrollmentCount;

    public CourseSessionStatsDTO(int courseSessionId, String courseSessionCode, long enrollmentCount) {
        this.courseSessionId = courseSessionId;
        this.courseSessionCode = courseSessionCode;
        this.enrollmentCount = enrollmentCount;
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
     * @return the courseSessionCode
     */
    public String getCourseSessionCode() {
        return courseSessionCode;
    }

    /**
     * @param courseSessionCode the courseSessionCode to set
     */
    public void setCourseSessionCode(String courseSessionCode) {
        this.courseSessionCode = courseSessionCode;
    }

    /**
     * @return the enrollmentCount
     */
    public long getEnrollmentCount() {
        return enrollmentCount;
    }

    /**
     * @param enrollmentCount the enrollmentCount to set
     */
    public void setEnrollmentCount(long enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }

    
}
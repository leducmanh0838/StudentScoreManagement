package com.ldm.dto;

public class StudentInCourseDTO {
    private int userId;
    private String firstName;
    private String lastName;
    private String userCode;
    private int enrollmentId;

    public StudentInCourseDTO(int userId, String firstName, String lastName, String userCode, int enrollmentId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userCode = userCode;
        this.enrollmentId = enrollmentId;
    }

    public StudentInCourseDTO() {
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the userCode
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * @param userCode the userCode to set
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
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

    
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.dto;

/**
 *
 * @author PC
 */
public class GradeStatsDTO {
    private int courseSessionId;
    private String courseSessionCode;
    private String gradeStatus;
    private String teacherName;
    private long excellentCount;
    private long goodCount;
    private long averageCount;
    private long weakCount;

    public GradeStatsDTO(int courseSessionId, String courseSessionCode, String gradeStatus, String teacherName, long excellentCount, long goodCount, long averageCount, long weakCount) {
        this.courseSessionId = courseSessionId;
        this.courseSessionCode = courseSessionCode;
        this.gradeStatus = gradeStatus;
        this.teacherName = teacherName;
        this.excellentCount = excellentCount;
        this.goodCount = goodCount;
        this.averageCount = averageCount;
        this.weakCount = weakCount;
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
     * @return the gradeStatus
     */
    public String getGradeStatus() {
        return gradeStatus;
    }

    /**
     * @param gradeStatus the gradeStatus to set
     */
    public void setGradeStatus(String gradeStatus) {
        this.gradeStatus = gradeStatus;
    }

    /**
     * @return the excellentCount
     */
    public long getExcellentCount() {
        return excellentCount;
    }

    /**
     * @param excellentCount the excellentCount to set
     */
    public void setExcellentCount(long excellentCount) {
        this.excellentCount = excellentCount;
    }

    /**
     * @return the goodCount
     */
    public long getGoodCount() {
        return goodCount;
    }

    /**
     * @param goodCount the goodCount to set
     */
    public void setGoodCount(long goodCount) {
        this.goodCount = goodCount;
    }

    /**
     * @return the averageCount
     */
    public long getAverageCount() {
        return averageCount;
    }

    /**
     * @param averageCount the averageCount to set
     */
    public void setAverageCount(long averageCount) {
        this.averageCount = averageCount;
    }

    /**
     * @return the weakCount
     */
    public long getWeakCount() {
        return weakCount;
    }

    /**
     * @param weakCount the weakCount to set
     */
    public void setWeakCount(long weakCount) {
        this.weakCount = weakCount;
    }

    /**
     * @return the teacherName
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * @param teacherName the teacherName to set
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    
    
}

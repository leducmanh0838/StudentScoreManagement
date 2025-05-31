package com.ldm.dto;

import com.ldm.pojo.Grade;

public class GradeInfoDTO {
    private int gradeId;
    private float score;
    private int enrollmentId;
    private int criteriaId;

    public GradeInfoDTO(int gradeId, float score, int enrollmentId, int criteriaId) {
        this.gradeId = gradeId;
        this.score = score;
        this.enrollmentId = enrollmentId;
        this.criteriaId = criteriaId;
    }
    
    public GradeInfoDTO(Grade grade) {
        this.gradeId = grade.getId();
        this.score = grade.getScore();
        this.enrollmentId = grade.getEnrollmentId().getId();
        this.criteriaId = grade.getCriteriaId().getId();
    }

    /**
     * @return the gradeId
     */
    public int getGradeId() {
        return gradeId;
    }

    /**
     * @param gradeId the gradeId to set
     */
    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    /**
     * @return the score
     */
    public float getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(float score) {
        this.score = score;
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
     * @return the criteriaId
     */
    public int getCriteriaId() {
        return criteriaId;
    }

    /**
     * @param criteriaId the criteriaId to set
     */
    public void setCriteriaId(int criteriaId) {
        this.criteriaId = criteriaId;
    }

    
}
package com.ldm.dto;

import java.util.List;

public class EnrollmentGradeDTO {
    private int enrollmentId;
    private List<ScoreDTO> scores;

    public EnrollmentGradeDTO(int enrollmentId, List<ScoreDTO> scores) {
        this.enrollmentId = enrollmentId;
        this.scores = scores;
    }

    public EnrollmentGradeDTO() {
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
     * @return the scores
     */
    public List<ScoreDTO> getScores() {
        return scores;
    }

    /**
     * @param scores the scores to set
     */
    public void setScores(List<ScoreDTO> scores) {
        this.scores = scores;
    }

    
}
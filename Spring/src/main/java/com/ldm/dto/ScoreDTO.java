package com.ldm.dto;

public class ScoreDTO {
    private int criteriaId;
    private float score;
    
    public ScoreDTO(int criteriaId, float score) {
        this.criteriaId = criteriaId;
        this.score = score;
    }

    public ScoreDTO() {
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
    
    
}
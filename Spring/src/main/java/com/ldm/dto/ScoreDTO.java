package com.ldm.dto;

public class ScoreDTO {
    private Integer criteriaId;
    private Float score;

    /**
     * @return the criteriaId
     */
    public Integer getCriteriaId() {
        return criteriaId;
    }

    /**
     * @param criteriaId the criteriaId to set
     */
    public void setCriteriaId(Integer criteriaId) {
        this.criteriaId = criteriaId;
    }

    /**
     * @return the score
     */
    public Float getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(Float score) {
        this.score = score;
    }

    
}
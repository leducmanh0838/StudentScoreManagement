package com.ldm.dto;

import java.util.List;

public class StudentGradeDTO {
    private Integer studentId;
    private List<ScoreDTO> scores;
    
    
    // Getters & Setters

    /**
     * @return the studentId
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
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
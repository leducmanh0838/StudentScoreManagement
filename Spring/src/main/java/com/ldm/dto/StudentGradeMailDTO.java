package com.ldm.dto;

import java.util.ArrayList;
import java.util.List;

public class StudentGradeMailDTO {
    private String studentEmail;
    private List<ScoreAndCriteriaDTO> scores;

    public StudentGradeMailDTO(String studentEmail) {
        this.studentEmail = studentEmail;
        this.scores = new ArrayList<>();
    }

    public void addScore(String criteriaName, Float score, Integer weight) {
        this.getScores().add(new ScoreAndCriteriaDTO(criteriaName, score, weight));
    }

    /**
     * @return the studentEmail
     */
    public String getStudentEmail() {
        return studentEmail;
    }

    /**
     * @param studentEmail the studentEmail to set
     */
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    /**
     * @return the scores
     */
    public List<ScoreAndCriteriaDTO> getScores() {
        return scores;
    }

    /**
     * @param scores the scores to set
     */
    public void setScores(List<ScoreAndCriteriaDTO> scores) {
        this.scores = scores;
    }
    
    
}
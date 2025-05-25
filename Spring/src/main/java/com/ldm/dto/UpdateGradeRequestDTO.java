package com.ldm.dto;

import java.util.List;

public class UpdateGradeRequestDTO {
    private int courseSessionId;
    private List<ScoreUpdateDTO> scores;

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
     * @return the scores
     */
    public List<ScoreUpdateDTO> getScores() {
        return scores;
    }

    /**
     * @param scores the scores to set
     */
    public void setScores(List<ScoreUpdateDTO> scores) {
        this.scores = scores;
    }


}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.dto;

import com.ldm.pojo.Grade;

/**
 *
 * @author PC
 */
public class GradeReponseDTO {
    private Integer id;
    private float score;
    private Integer enrollmentId;
    private Integer criteriaId;

    public GradeReponseDTO() {
    }

    public GradeReponseDTO(Grade grade) {
        this.id = grade.getId();
        this.score = grade.getScore();
        this.enrollmentId = grade.getEnrollmentId().getId();
        this.criteriaId = grade.getCriteriaId().getId();
    }
    
    public GradeReponseDTO(GradeInfoDTO grade) {
        this.id = grade.getGradeId();
        this.score = grade.getScore();
        this.enrollmentId = grade.getEnrollmentId();
        this.criteriaId = grade.getCriteriaId();
    }
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    /**
     * @param enrollmentId the enrollmentId to set
     */
    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

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
    
    
}

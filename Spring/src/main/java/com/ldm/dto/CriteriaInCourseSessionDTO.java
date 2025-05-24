/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.dto;

import com.ldm.pojo.Criteria;

/**
 *
 * @author PC
 */
public class CriteriaInCourseSessionDTO {
     private Integer id;
    private String criteriaName;
    private Integer weight;

    // Constructor
    public CriteriaInCourseSessionDTO(Criteria c) {
        this.id = c.getId();
        this.criteriaName = c.getCriteriaName();
        this.weight = c.getWeight();
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
     * @return the criteriaName
     */
    public String getCriteriaName() {
        return criteriaName;
    }

    /**
     * @param criteriaName the criteriaName to set
     */
    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    /**
     * @return the weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    
}

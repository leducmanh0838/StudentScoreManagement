/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.dto;

/**
 *
 * @author PC
 */
public class CreateCriteriaDTO {
    private String criteriaName;
    private String type;
    private Float weight;

    public CreateCriteriaDTO(String criteriaName, String type, Float weight) {
        this.criteriaName = criteriaName;
        this.type = type;
        this.weight = weight;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the weight
     */
    public Float getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(Float weight) {
        this.weight = weight;
    }
    
    
}

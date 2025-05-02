/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "additional_grade")
@NamedQueries({
    @NamedQuery(name = "AdditionalGrade.findAll", query = "SELECT a FROM AdditionalGrade a"),
    @NamedQuery(name = "AdditionalGrade.findById", query = "SELECT a FROM AdditionalGrade a WHERE a.id = :id"),
    @NamedQuery(name = "AdditionalGrade.findByGradeName", query = "SELECT a FROM AdditionalGrade a WHERE a.gradeName = :gradeName"),
    @NamedQuery(name = "AdditionalGrade.findByScore", query = "SELECT a FROM AdditionalGrade a WHERE a.score = :score"),
    @NamedQuery(name = "AdditionalGrade.findByWeight", query = "SELECT a FROM AdditionalGrade a WHERE a.weight = :weight")})
public class AdditionalGrade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "grade_name")
    private String gradeName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "score")
    private float score;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "weight")
    private Float weight;
    @JoinColumn(name = "grade_id", referencedColumnName = "id")
    @ManyToOne
    private Grade gradeId;

    public AdditionalGrade() {
    }

    public AdditionalGrade(Integer id) {
        this.id = id;
    }

    public AdditionalGrade(Integer id, String gradeName, float score) {
        this.id = id;
        this.gradeName = gradeName;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Grade getGradeId() {
        return gradeId;
    }

    public void setGradeId(Grade gradeId) {
        this.gradeId = gradeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdditionalGrade)) {
            return false;
        }
        AdditionalGrade other = (AdditionalGrade) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ldm.pojo.AdditionalGrade[ id=" + id + " ]";
    }
    
}

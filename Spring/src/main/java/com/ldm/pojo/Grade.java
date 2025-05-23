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
import java.io.Serializable;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "grade")
@NamedQueries({
    @NamedQuery(name = "Grade.findAll", query = "SELECT g FROM Grade g"),
    @NamedQuery(name = "Grade.findById", query = "SELECT g FROM Grade g WHERE g.id = :id"),
    @NamedQuery(name = "Grade.findByScore", query = "SELECT g FROM Grade g WHERE g.score = :score")})
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "score")
    private float score;
    @JoinColumn(name = "criteria_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Criteria criteriaId;
    @JoinColumn(name = "enrollment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Enrollment enrollmentId;

    public Grade() {
    }

    public Grade(Integer id) {
        this.id = id;
    }

    public Grade(Integer id, float score) {
        this.id = id;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Criteria getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(Criteria criteriaId) {
        this.criteriaId = criteriaId;
    }

    public Enrollment getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Enrollment enrollmentId) {
        this.enrollmentId = enrollmentId;
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
        if (!(object instanceof Grade)) {
            return false;
        }
        Grade other = (Grade) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ldm.pojo.Grade[ id=" + id + " ]";
    }
    
}

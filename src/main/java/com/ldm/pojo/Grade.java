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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "grade")
@NamedQueries({
    @NamedQuery(name = "Grade.findAll", query = "SELECT g FROM Grade g"),
    @NamedQuery(name = "Grade.findById", query = "SELECT g FROM Grade g WHERE g.id = :id"),
    @NamedQuery(name = "Grade.findByMidtermScore", query = "SELECT g FROM Grade g WHERE g.midtermScore = :midtermScore"),
    @NamedQuery(name = "Grade.findByMidtermWeight", query = "SELECT g FROM Grade g WHERE g.midtermWeight = :midtermWeight"),
    @NamedQuery(name = "Grade.findByFinalScore", query = "SELECT g FROM Grade g WHERE g.finalScore = :finalScore"),
    @NamedQuery(name = "Grade.findByStatus", query = "SELECT g FROM Grade g WHERE g.status = :status"),
    @NamedQuery(name = "Grade.findByIsActive", query = "SELECT g FROM Grade g WHERE g.isActive = :isActive"),
    @NamedQuery(name = "Grade.findByCreatedDate", query = "SELECT g FROM Grade g WHERE g.createdDate = :createdDate"),
    @NamedQuery(name = "Grade.findByUpdatedDate", query = "SELECT g FROM Grade g WHERE g.updatedDate = :updatedDate")})
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "midterm_score")
    private Float midtermScore;
    @Column(name = "midterm_weight")
    private Float midtermWeight;
    @Column(name = "final_score")
    private Float finalScore;
    @Size(max = 6)
    @Column(name = "status")
    private String status;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JoinColumn(name = "enrollment_id", referencedColumnName = "id")
    @ManyToOne
    private Enrollment enrollmentId;
    @OneToMany(mappedBy = "gradeId")
    private Set<AdditionalGrade> additionalGradeSet;

    public Grade() {
    }

    public Grade(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(Float midtermScore) {
        this.midtermScore = midtermScore;
    }

    public Float getMidtermWeight() {
        return midtermWeight;
    }

    public void setMidtermWeight(Float midtermWeight) {
        this.midtermWeight = midtermWeight;
    }

    public Float getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Float finalScore) {
        this.finalScore = finalScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Enrollment getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Enrollment enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Set<AdditionalGrade> getAdditionalGradeSet() {
        return additionalGradeSet;
    }

    public void setAdditionalGradeSet(Set<AdditionalGrade> additionalGradeSet) {
        this.additionalGradeSet = additionalGradeSet;
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

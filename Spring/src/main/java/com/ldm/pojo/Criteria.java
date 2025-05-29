/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "criteria")
@NamedQueries({
    @NamedQuery(name = "Criteria.findAll", query = "SELECT c FROM Criteria c"),
    @NamedQuery(name = "Criteria.findById", query = "SELECT c FROM Criteria c WHERE c.id = :id"),
    @NamedQuery(name = "Criteria.findByCriteriaName", query = "SELECT c FROM Criteria c WHERE c.criteriaName = :criteriaName"),
    @NamedQuery(name = "Criteria.findByWeight", query = "SELECT c FROM Criteria c WHERE c.weight = :weight")})
public class Criteria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "criteria_name")
    private String criteriaName;
    @Column(name = "weight")
    private Integer weight;
    @JoinColumn(name = "course_session_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CourseSession courseSessionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "criteriaId")
    private Set<Grade> gradeSet;

    public Criteria() {
    }

    public Criteria(Integer id) {
        this.id = id;
    }

    public Criteria(Integer id, String criteriaName) {
        this.id = id;
        this.criteriaName = criteriaName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public CourseSession getCourseSessionId() {
        return courseSessionId;
    }

    public void setCourseSessionId(CourseSession courseSessionId) {
        this.courseSessionId = courseSessionId;
    }

    public Set<Grade> getGradeSet() {
        return gradeSet;
    }

    public void setGradeSet(Set<Grade> gradeSet) {
        this.gradeSet = gradeSet;
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
        if (!(object instanceof Criteria)) {
            return false;
        }
        Criteria other = (Criteria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ldm.pojo.Criteria[ id=" + id + " ]";
    }
    
}

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "course_session")
@NamedQueries({
    @NamedQuery(name = "CourseSession.findAll", query = "SELECT c FROM CourseSession c"),
    @NamedQuery(name = "CourseSession.findById", query = "SELECT c FROM CourseSession c WHERE c.id = :id"),
    @NamedQuery(name = "CourseSession.findByCode", query = "SELECT c FROM CourseSession c WHERE c.code = :code"),
    @NamedQuery(name = "CourseSession.findByIsOpen", query = "SELECT c FROM CourseSession c WHERE c.isOpen = :isOpen"),
    @NamedQuery(name = "CourseSession.findByGradeStatus", query = "SELECT c FROM CourseSession c WHERE c.gradeStatus = :gradeStatus"),
    @NamedQuery(name = "CourseSession.findByIsActive", query = "SELECT c FROM CourseSession c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "CourseSession.findByCreatedDate", query = "SELECT c FROM CourseSession c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "CourseSession.findByUpdatedDate", query = "SELECT c FROM CourseSession c WHERE c.updatedDate = :updatedDate")})
public class CourseSession implements Serializable {
    public static final String DRAFT = "Draft";
    public static final String LOCKED = "Locked";

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "code")
    private String code;
    @Column(name = "is_open")
    private Boolean isOpen;
    @Size(max = 6)
    @Column(name = "grade_status")
    private String gradeStatus;
    @Column(name = "is_active", insertable = false, updatable = false)
    private Boolean isActive;
    @Column(name = "created_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "updated_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(mappedBy = "courseSessionId")
    private Set<ForumPost> forumPostSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courseSessionId")
    private Set<Criteria> criteriaSet;
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Course courseId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User teacherId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courseSessionId")
    private Set<Enrollment> enrollmentSet;

    public CourseSession() {
    }

    public CourseSession(Integer id) {
        this.id = id;
    }

    public CourseSession(Integer id, String code) {
        this.id = id;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getGradeStatus() {
        return gradeStatus;
    }

    public void setGradeStatus(String gradeStatus) {
        this.gradeStatus = gradeStatus;
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

    public Set<ForumPost> getForumPostSet() {
        return forumPostSet;
    }

    public void setForumPostSet(Set<ForumPost> forumPostSet) {
        this.forumPostSet = forumPostSet;
    }

    public Set<Criteria> getCriteriaSet() {
        return criteriaSet;
    }

    public void setCriteriaSet(Set<Criteria> criteriaSet) {
        this.criteriaSet = criteriaSet;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    public User getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(User teacherId) {
        this.teacherId = teacherId;
    }

    public Set<Enrollment> getEnrollmentSet() {
        return enrollmentSet;
    }

    public void setEnrollmentSet(Set<Enrollment> enrollmentSet) {
        this.enrollmentSet = enrollmentSet;
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
        if (!(object instanceof CourseSession)) {
            return false;
        }
        CourseSession other = (CourseSession) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ldm.pojo.CourseSession[ id=" + id + " ]";
    }
    
}

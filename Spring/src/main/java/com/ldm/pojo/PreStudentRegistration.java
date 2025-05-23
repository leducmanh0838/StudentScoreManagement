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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "pre_student_registration")
@NamedQueries({
    @NamedQuery(name = "PreStudentRegistration.findAll", query = "SELECT p FROM PreStudentRegistration p"),
    @NamedQuery(name = "PreStudentRegistration.findById", query = "SELECT p FROM PreStudentRegistration p WHERE p.id = :id"),
    @NamedQuery(name = "PreStudentRegistration.findByEmail", query = "SELECT p FROM PreStudentRegistration p WHERE p.email = :email"),
    @NamedQuery(name = "PreStudentRegistration.findByPassword", query = "SELECT p FROM PreStudentRegistration p WHERE p.password = :password"),
    @NamedQuery(name = "PreStudentRegistration.findByFirstName", query = "SELECT p FROM PreStudentRegistration p WHERE p.firstName = :firstName"),
    @NamedQuery(name = "PreStudentRegistration.findByLastName", query = "SELECT p FROM PreStudentRegistration p WHERE p.lastName = :lastName"),
    @NamedQuery(name = "PreStudentRegistration.findByAvatar", query = "SELECT p FROM PreStudentRegistration p WHERE p.avatar = :avatar"),
    @NamedQuery(name = "PreStudentRegistration.findByOtp", query = "SELECT p FROM PreStudentRegistration p WHERE p.otp = :otp"),
    @NamedQuery(name = "PreStudentRegistration.findByOtpExpiration", query = "SELECT p FROM PreStudentRegistration p WHERE p.otpExpiration = :otpExpiration"),
    @NamedQuery(name = "PreStudentRegistration.findByVerificationAttempts", query = "SELECT p FROM PreStudentRegistration p WHERE p.verificationAttempts = :verificationAttempts"),
    @NamedQuery(name = "PreStudentRegistration.findByCreatedDate", query = "SELECT p FROM PreStudentRegistration p WHERE p.createdDate = :createdDate")})
public class PreStudentRegistration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;
    @Size(max = 6)
    @Column(name = "otp")
    private String otp;
    @Column(name = "otp_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otpExpiration;
    @Column(name = "verification_attempts")
    private Integer verificationAttempts;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public PreStudentRegistration() {
    }

    public PreStudentRegistration(Integer id) {
        this.id = id;
    }

    public PreStudentRegistration(Integer id, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getOtpExpiration() {
        return otpExpiration;
    }

    public void setOtpExpiration(Date otpExpiration) {
        this.otpExpiration = otpExpiration;
    }

    public Integer getVerificationAttempts() {
        return verificationAttempts;
    }

    public void setVerificationAttempts(Integer verificationAttempts) {
        this.verificationAttempts = verificationAttempts;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
        if (!(object instanceof PreStudentRegistration)) {
            return false;
        }
        PreStudentRegistration other = (PreStudentRegistration) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ldm.pojo.PreStudentRegistration[ id=" + id + " ]";
    }
    
}

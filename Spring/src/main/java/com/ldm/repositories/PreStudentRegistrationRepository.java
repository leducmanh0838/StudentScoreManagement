/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories;

import com.ldm.pojo.PreStudentRegistration;

/**
 *
 * @author PC
 */
public interface PreStudentRegistrationRepository {
    PreStudentRegistration addOrUpdate(PreStudentRegistration p);
    PreStudentRegistration findByEmail(String email);
    void delete(PreStudentRegistration p);
}

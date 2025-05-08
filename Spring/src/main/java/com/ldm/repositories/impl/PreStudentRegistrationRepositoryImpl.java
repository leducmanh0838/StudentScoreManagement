/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.pojo.PreStudentRegistration;
import com.ldm.repositories.PreStudentRegistrationRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author PC
 */
@Repository
@Transactional
public class PreStudentRegistrationRepositoryImpl implements PreStudentRegistrationRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public PreStudentRegistration addOrUpdate(PreStudentRegistration p) {
        Session s = this.factory.getObject().getCurrentSession();

        // Tìm xem email đã tồn tại chưa
        PreStudentRegistration existing = findByEmail(p.getEmail());

        if (existing != null) {
            // Cập nhật lại các trường cần thiết
            existing.setOtp(p.getOtp());
            existing.setOtpExpiration(p.getOtpExpiration());
            existing.setPassword(p.getPassword());
            existing.setCreatedDate(p.getCreatedDate());
            existing.setFirstName(p.getFirstName());
            existing.setLastName(p.getLastName());
            existing.setAvatar(p.getAvatar());
            // Cập nhật record
            s.merge(existing);
            return existing;
        } else {
            // Email chưa tồn tại, tạo mới
            s.persist(p);
            return p;
        }
    }

    // Tìm theo email
    public PreStudentRegistration findByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<PreStudentRegistration> query = s.createQuery(
            "FROM PreStudentRegistration WHERE email = :email", PreStudentRegistration.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }
    
}

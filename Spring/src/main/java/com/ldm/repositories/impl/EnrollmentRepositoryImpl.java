/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.pojo.Enrollment;
import com.ldm.repositories.EnrollmentRepository;
import org.hibernate.Session;
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
public class EnrollmentRepositoryImpl implements EnrollmentRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Enrollment addOrUpdate(Enrollment enrollment) {
        Session s = this.factory.getObject().getCurrentSession();
        if(enrollment.getId()==null){
            s.persist(enrollment);
        }
        else{
            s.merge(enrollment);
        }
        return enrollment;
    }
    
    @Override
    public boolean existsByUserIdAndCourseSessionId(int userId, int courseSessionId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "SELECT COUNT(e.id) FROM Enrollment e WHERE e.userId.id = :userId AND e.courseSessionId.id = :courseSessionId";
        Long count = session.createQuery(hql, Long.class)
                            .setParameter("userId", userId)
                            .setParameter("courseSessionId", courseSessionId)
                            .uniqueResult();

        return count != null && count > 0;
    }
//@Override
//    public CourseSession addOrUpdate(CourseSession cs) {
//        Session s = this.factory.getObject().getCurrentSession();
//        if(cs.getId()==null){
//            s.persist(cs);
//        }
//        else{
//            s.merge(cs);
//        }
//        
//        return cs;
//    }    

}

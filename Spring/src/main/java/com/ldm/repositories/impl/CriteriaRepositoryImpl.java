/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.configs.LoggerConfig;
import com.ldm.pojo.Criteria;
import com.ldm.repositories.CriteriaRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CriteriaRepositoryImpl implements CriteriaRepository{
    private static final Logger logger = LoggerFactory.getLogger(CriteriaRepositoryImpl.class);
    public static final int BATCH_SIZE = 20;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Criteria addOrUpdate(Criteria criteria) {
        Session s = this.factory.getObject().getCurrentSession();
        if(criteria.getId()==null){
            s.persist(criteria);
        }
        else{
            s.merge(criteria);
        }
        return criteria;
    }

    @Override
    public List<Criteria> getCriteriaByCourseSesion(Integer courseSessionId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Criteria> q = s.createQuery(
                "FROM Criteria", Criteria.class
        );
        return q.getResultList();
    }
    
    @Override
    public List<Criteria> addList(List<Criteria> criteriaList) {
        LoggerConfig.info("Bắt đầu công việc với id={}", 123);
        logger.info(String.format("DEBUG: %d", criteriaList.size()));
//        System.out.printf("DEBUG: %d", criteriaList.size());
        Session session = this.factory.getObject().getCurrentSession();

        for (int i = 0; i < criteriaList.size(); i++) {
            Criteria c = criteriaList.get(i);
            session.persist(c);

            // Flush + clear mỗi batchSize phần tử để tránh memory leak
//            if (i % BATCH_SIZE == 0 && i > 0) {
//                session.flush(); // ghi xuống DB
//                session.clear(); // dọn dẹp session cache
//            }
        }

        // Flush lần cuối để đảm bảo mọi thứ được ghi vào DB
//        session.flush();
//        session.clear();

        return criteriaList;
    }
}

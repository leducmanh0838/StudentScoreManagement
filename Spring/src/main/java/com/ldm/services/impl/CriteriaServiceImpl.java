/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.pojo.Criteria;
import com.ldm.repositories.CriteriaRepository;
import com.ldm.services.CriteriaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class CriteriaServiceImpl implements CriteriaService{
    @Autowired
    private CriteriaRepository criteriaRepository;
    
    @Override
    public Criteria addOrUpdate(Criteria criteria) {
        return criteriaRepository.addOrUpdate(criteria);
    }

//    @Override
//    public List<Criteria> getCriteriaByCourseSession(Integer courseSessionId) {
//        return CriteriaRepository.getCriteriaByCourseSesion(courseSessionId);
//    }

    @Override
    public List<Criteria> getCriteriaByCourseSession(Integer courseSessionId) {
        return criteriaRepository.getCriteriaByCourseSesion(courseSessionId);
    }

    @Override
    public List<Criteria> addList(List<Criteria> criteriaList) {
        return criteriaRepository.addList(criteriaList);
    }
    
}

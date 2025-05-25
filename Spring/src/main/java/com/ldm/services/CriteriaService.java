/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services;

import com.ldm.pojo.Criteria;
import java.util.List;

/**
 *
 * @author PC
 */
public interface CriteriaService {
    Criteria addOrUpdate(Criteria criteria);
    List<Criteria> getCriteriaByCourseSession(Integer courseSessionId);
    List<Criteria> addList(List<Criteria> criteriaList, Integer courseSessionId);
}

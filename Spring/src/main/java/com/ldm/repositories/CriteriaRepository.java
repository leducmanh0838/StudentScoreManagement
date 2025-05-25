/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories;

import com.ldm.pojo.Criteria;
import java.util.List;

/**
 *
 * @author PC
 */
public interface CriteriaRepository {
    Criteria addOrUpdate(Criteria criteria);
    List<Criteria> getCriteriaByCourseSesion(Integer courseSessionId);
    List<Criteria> addList(List<Criteria> criteriaList);
    boolean hasCriteriaInCourseSession(Integer courseSessionId);
    
}

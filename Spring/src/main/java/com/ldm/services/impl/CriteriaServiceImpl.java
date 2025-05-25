/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.pojo.CourseSession;
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
public class CriteriaServiceImpl implements CriteriaService {
    private static final int CRITERIA_SIZE=5;

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
    public List<Criteria> addList(List<Criteria> criteriaList, Integer courseSessionId) {
        // Kiểm tra không vượt quá 5 tiêu chí
        if (criteriaList.size() > CRITERIA_SIZE) {
            throw new IllegalArgumentException(String.format("Không được thêm quá %d tiêu chí", CRITERIA_SIZE));
        }
        // Kiểm tra có đúng 1 "midterm" và 1 "final"
        long midtermCount = criteriaList.stream()
                .filter(c -> "midterm".equalsIgnoreCase(c.getCriteriaName()))
                .count();

        long finalCount = criteriaList.stream()
                .filter(c -> "final".equalsIgnoreCase(c.getCriteriaName()))
                .count();

        if (midtermCount != 1 || finalCount != 1) {
            throw new IllegalArgumentException("Danh sách phải chứa đúng 1 'midterm' và 1 'final'");
        }
        
        // Kiểm tra weight >= 0
        for (Criteria c : criteriaList) {
            if (c.getWeight() <= 0) {
                throw new IllegalArgumentException("Trọng số (weight) phải lớn hơn 0 cho mỗi tiêu chí");
            }
        }

        // Kiểm tra tổng weight = 100
        int totalWeight = criteriaList.stream()
                .mapToInt(Criteria::getWeight)
                .sum();

        if (totalWeight != 100) {
            throw new IllegalArgumentException("Tổng trọng số (weight) phải bằng 100");
        }
        
        // kiểm tra buổi học đã có tiêu chí
        if (criteriaRepository.hasCriteriaInCourseSession(courseSessionId)) {
            throw new IllegalArgumentException("Đã có tiêu chí rồi!");
        }
        
        // bổ sung criteral 1 buổi học
        CourseSession courseSession = new CourseSession(courseSessionId);
        for (Criteria c : criteriaList) {
            c.setCourseSessionId(courseSession);
        }
        
        return criteriaRepository.addList(criteriaList);
    }

}

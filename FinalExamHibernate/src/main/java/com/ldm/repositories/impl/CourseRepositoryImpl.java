/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.finalexamhibernate.HibernateUtils;
import com.ldm.pojo.Course;
import com.ldm.repositories.CourseRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

/**
 *
 * @author PC
 */
public class CourseRepositoryImpl implements CourseRepository {

    public static final int PAGE_SIZE = 6;

    @Override
    public Course addOrUpdate(Course course) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            if (course.getId() == null) {
                s.persist(course);
            } else {
                s.merge(course);
            }

            return course;
        }
    }

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder b = s.getCriteriaBuilder();
            CriteriaQuery<Course> q = b.createQuery(Course.class);
            Root<Course> root = q.from(Course.class);
            q.select(root);

            if (params != null && params.containsKey("name")) {
                List<Predicate> predicates = new ArrayList<>();
                String name = params.get("name");
                if (name != null && !name.isEmpty()) {
                    predicates.add(b.like(root.get("name"), String.format("%%%s%%", name)));
                }
                q.where(predicates.toArray(Predicate[]::new));

                String orderBy = params.get("orderBy");
                if (orderBy != null && !orderBy.isEmpty()) {
                    q.orderBy(b.asc(root.get(orderBy)));
                }
            }

            Query query = s.createQuery(q);
            if (params != null && params.containsKey("page")) {
                int page = Integer.parseInt(params.get("page"));
                int start = (page - 1) * PAGE_SIZE;

                query.setMaxResults(PAGE_SIZE);
                query.setFirstResult(start);
            } else {
                int page = 1; // Mặc định trang 1
                int start = (page - 1) * PAGE_SIZE;
                query.setMaxResults(PAGE_SIZE);
                query.setFirstResult(start);
            }
            return query.getResultList();
        }
    }
}

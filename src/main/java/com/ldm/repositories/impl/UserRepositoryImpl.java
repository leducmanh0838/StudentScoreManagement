/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.configs.VariableConfig;
import com.ldm.pojo.User;
import org.hibernate.Session;
import jakarta.persistence.Query;
import com.ldm.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;

/**
 *
 * @author PC
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    public static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<User> getAllUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User", User.class);
        return q.getResultList();
    }

    @Override
    public List<User> getUsersForStaff(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root<User> root = q.from(User.class);
        q.select(root);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.notEqual(root.get("role"), VariableConfig.STAFF_ROLE));

        if (params != null) {

            // Lọc theo tên (first_name hoặc last_name)
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                String namePattern = String.format("%%%s%%", name);
                predicates.add(b.or(
                        b.like(root.get("firstName"), namePattern),
                        b.like(root.get("lastName"), namePattern)
                ));
            }

            // Lọc theo mã số người dùng (user_code)
            String userCode = params.get("userCode");
            if (userCode != null && !userCode.isEmpty()) {
                predicates.add(b.equal(root.get("userCode"), userCode));
            }

            // Lọc theo role
            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(b.equal(root.get("role"), role));
            }

            
        }
        // Thêm các điều kiện lọc vào câu truy vấn
        q.where(predicates.toArray(Predicate[]::new));

        Query query = s.createQuery(q);

        // Phân trang nếu có tham số "page"
        if (params != null && params.containsKey("page")) {
            // Nếu có tham số "page", lấy giá trị của nó
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;
            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        } else {
            // Nếu không có tham số "page", mặc định là trang 1
            int page = 1; // Mặc định trang 1
            int start = (page - 1) * PAGE_SIZE;
            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }
    
    @Override
    public User addOrUpdateTeacher(User teacher) {
        // Lấy phiên làm việc (session)
        Session s = this.factory.getObject().getCurrentSession();

        // Nếu ID của giảng viên là null, tức là giảng viên mới, thực hiện thêm mới
        if (teacher.getId() == null) {
            teacher.setRole("teacher");
            s.persist(teacher); // Thêm mới giảng viên vào cơ sở dữ liệu
        } else {
            // Nếu giảng viên đã có ID, thực hiện cập nhật thông tin
            s.merge(teacher); // Cập nhật giảng viên vào cơ sở dữ liệu
        }

        return teacher; // Trả về giảng viên đã được thêm hoặc cập nhật
    }
    
    @Override
    public User getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(User.class, id); // Dùng Hibernate để lấy theo primary key
    }
}

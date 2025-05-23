/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.pojo.Enrollment;
import com.ldm.pojo.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.ldm.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User", User.class);
        return q.getResultList();
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root<User> root = q.from(User.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.notEqual(root.get("role"), User.STAFF_ROLE));
        predicates.add(b.notEqual(root.get("role"), User.ADMIN_ROLE));

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
    public User getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(User.class, id); // Dùng Hibernate để lấy theo primary key
    }

    @Override
    public User addOrUpdateUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        if (u.getId() == null) {
            s.persist(u);
        } else {
            User existing = s.get(User.class, u.getId());
            if (existing != null) {
                // 2. Chỉ cập nhật nếu != null
                if (u.getFirstName()!= null)
                    existing.setFirstName(u.getFirstName());
                if (u.getLastName()!= null)
                    existing.setLastName(u.getLastName());
                if (u.getAvatar()!= null)
                    existing.setAvatar(u.getAvatar());
                // ... thêm các field khác tương tự
                s.merge(existing); // cập nhật lại bản đã sửa
                return existing;
            }
        }
        return u;
    }

    @Override
    public User getUserByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<User> q = s.createQuery("FROM User WHERE email = :email", User.class);
        q.setParameter("email", email);
        return q.uniqueResult(); // Trả về 1 user nếu tồn tại, null nếu không
    }

    @Override
    public boolean authenticate(String email, String password) {
        User u = this.getUserByEmail(email);

        // Kiểm tra null
        if (u == null || password == null || u.getPassword() == null) {
            return false;
        }

        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public List<Object[]> getAllTeacherNames() {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Object[]> q = s.createQuery(
                "SELECT u.id, CONCAT(u.firstName, ' ', u.lastName) FROM User u WHERE u.role = :role", Object[].class);
        q.setParameter("role", "teacher");
        return q.getResultList();
    }

    @Override
    public boolean isEmailExists(String email) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> query = session.createQuery(
            "SELECT COUNT(u.id) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }
    
    @Override
    public List<User> getStudentsInCourseSession(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        // Truy vấn trên User
        Root<Enrollment> enrollRoot = cq.from(Enrollment.class);
        Join<Enrollment, User> userJoin = enrollRoot.join("userId");
        cq.select(userJoin);

        List<Predicate> predicates = new ArrayList<>();

        // Bắt buộc phải có courseSessionId
        if (params.containsKey("courseSessionId")) {
            Integer csId = Integer.parseInt(params.get("courseSessionId"));
            predicates.add(cb.equal(enrollRoot.get("courseSessionId").get("id"), csId));

        } else {
            throw new IllegalArgumentException("Thiếu tham số courseSessionId!");
        }

        // Lọc theo userCode nếu có
        if (params.containsKey("userCode")) {
            String userCode = params.get("userCode");
            if (!userCode.isEmpty()) {
                predicates.add(cb.equal(userJoin.get("userCode"), userCode));
            }
        }

        // Lọc theo tên (first_name hoặc last_name)
        if (params.containsKey("name")) {
            String name = params.get("name");
            if (!name.isEmpty()) {
                String namePattern = "%" + name + "%";
                predicates.add(cb.or(
                    cb.like(userJoin.get("firstName"), namePattern),
                    cb.like(userJoin.get("lastName"), namePattern)
                ));
            }
        }

        // Chỉ lấy sinh viên
        predicates.add(cb.equal(userJoin.get("role"), User.STUDENT_ROLE));

        // Ghép điều kiện
        cq.where(predicates.toArray(Predicate[]::new));
        cq.distinct(true); // Tránh trùng sinh viên

        Query<User> query = s.createQuery(cq);

        // Phân trang
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int start = (page - 1) * PAGE_SIZE;
        query.setFirstResult(start);
        query.setMaxResults(PAGE_SIZE);

        return query.getResultList();
    }
}

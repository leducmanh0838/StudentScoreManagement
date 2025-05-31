package com.ldm.repositories.impl;

import com.ldm.dto.CourseSessionStatsDTO;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.Enrollment;
import com.ldm.repositories.StatsRepository;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<CourseSessionStatsDTO> countEnrollmentsByCourse(int courseId) {
        Session session = factory.getObject().getCurrentSession();

        StoredProcedureQuery query = session
                .createStoredProcedureQuery("GetCourseSessionStats");

        query.registerStoredProcedureParameter("courseId", Integer.class, ParameterMode.IN);
        query.setParameter("courseId", courseId);

        List<Object[]> results = query.getResultList();

        List<CourseSessionStatsDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            dtos.add(new CourseSessionStatsDTO(
                    (Integer) row[0],
                    (String) row[1],
                    ((Number) row[2]).longValue()
            ));
        }

        return dtos;

//        Session session = factory.getObject().getCurrentSession();
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<CourseSessionStatsDTO> cq = cb.createQuery(CourseSessionStatsDTO.class);
//
//        Root<CourseSession> csRoot = cq.from(CourseSession.class);
//        Join<CourseSession, Enrollment> enrollmentJoin = csRoot.join("enrollmentSet", JoinType.LEFT);
//
//        // Lọc theo courseId
//        Predicate courseMatch = cb.equal(csRoot.get("courseId").get("id"), courseId);
//
//        // GROUP BY courseSession id và code
//        cq.select(cb.construct(
//                CourseSessionStatsDTO.class,
//                csRoot.get("id"),
//                csRoot.get("code"),
//                cb.count(enrollmentJoin.get("id"))
//        ))
//        .where(courseMatch)
//        .groupBy(csRoot.get("id"), csRoot.get("code"))
//        .orderBy(cb.asc(csRoot.get("id")));
//
//        return session.createQuery(cq).getResultList();
    }
}

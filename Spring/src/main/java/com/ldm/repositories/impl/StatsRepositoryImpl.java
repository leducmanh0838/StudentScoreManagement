package com.ldm.repositories.impl;

import com.ldm.dto.CourseSessionStatsDTO;
import com.ldm.dto.GradeStatsDTO;
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
import java.math.BigDecimal;
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
    public List<CourseSessionStatsDTO> countEnrollmentsByCourse(int courseId, Integer year) {
        Session session = factory.getObject().getCurrentSession();

        StoredProcedureQuery query = session
                .createStoredProcedureQuery("GetCourseSessionStats");

        query.registerStoredProcedureParameter("courseId", Integer.class, ParameterMode.IN);
        query.setParameter("courseId", courseId);
        query.registerStoredProcedureParameter("inputYear", Integer.class, ParameterMode.IN);
        query.setParameter("inputYear", year);

        List<Object[]> results = query.getResultList();

        List<CourseSessionStatsDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            dtos.add(new CourseSessionStatsDTO(
                    (Integer) row[0],
                    (String) row[1],
                    String.format("%s %s", (String) row[2], (String) row[3]),
                    ((Number) row[4]).longValue()
            ));
        }

        return dtos;
    }

    @Override
    public List<GradeStatsDTO> studentPerformanceStats(int courseId, Integer year) {
        Session session = factory.getObject().getCurrentSession();

        StoredProcedureQuery query = session
                .createStoredProcedureQuery("GetStudentPerformanceStats");

        query.registerStoredProcedureParameter("courseId", Integer.class, ParameterMode.IN);
        query.setParameter("courseId", courseId);
        query.registerStoredProcedureParameter("inputYear", Integer.class, ParameterMode.IN);
        query.setParameter("inputYear", year);

        List<Object[]> results = query.getResultList();

        List<GradeStatsDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            dtos.add(new GradeStatsDTO(
                    (Integer) row[0],
                    (String) row[1],
                    (String) row[2],
                    String.format("%s %s", (String) row[3], (String) row[4]),
                    ((BigDecimal) row[5]).intValue(),
                    ((BigDecimal) row[6]).intValue(),
                    ((BigDecimal) row[7]).intValue(),
                    ((BigDecimal) row[8]).intValue()
            ));
        }

        return dtos;
    }
}

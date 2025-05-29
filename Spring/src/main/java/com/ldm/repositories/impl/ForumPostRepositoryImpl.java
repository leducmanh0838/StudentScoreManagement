/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.dto.ForumPostInfoDTO;
import com.ldm.dto.UserNameAndAvatarDTO;
import com.ldm.pojo.ForumPost;
import com.ldm.repositories.ForumPostRepository;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
public class ForumPostRepositoryImpl implements ForumPostRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public ForumPost addOrUpdate(ForumPost forumPost) {
        Session s = this.factory.getObject().getCurrentSession();
        if (forumPost.getId() == null) {
            s.persist(forumPost);
        } else {
            s.merge(forumPost);
        }

        return forumPost;
    }

    @Override
    public List<ForumPost> getForumPostByCourseSession(Integer courseSessionId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<ForumPost> query = session.createQuery(
            "FROM ForumPost fp WHERE fp.courseSessionId.id = :courseSessionId",
            ForumPost.class
        );
        query.setParameter("courseSessionId", courseSessionId);
        return query.getResultList();
    }
    
    @Override
    public List<ForumPostInfoDTO> getForumPostDTOsByCourseSession(Integer courseSessionId) {
    Session session = this.factory.getObject().getCurrentSession();

    Query<Object[]> query = session.createQuery(
        "SELECT fp.id, u.id, u.firstName, u.lastName, u.avatar, fp.courseSessionId.id, fp.title, fp.content " +
        "FROM ForumPost fp JOIN fp.userId u WHERE fp.courseSessionId.id = :courseSessionId",
        Object[].class
    );
    query.setParameter("courseSessionId", courseSessionId);

    List<Object[]> results = query.getResultList();
    List<ForumPostInfoDTO> dtos = new ArrayList<>();

    for (Object[] row : results) {
        UserNameAndAvatarDTO userDTO = new UserNameAndAvatarDTO(
            (int) row[1],
            (String) row[2],
            (String) row[3],
            (String) row[4]
        );

        ForumPostInfoDTO postDTO = new ForumPostInfoDTO(
            (Integer) row[0],
            userDTO,
            (Integer) row[5],
            (String) row[6],
            (String) row[7]
        );

        dtos.add(postDTO);
    }

    return dtos;
}

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories.impl;

import com.ldm.dto.CommentInfoDTO;
import com.ldm.dto.UserNameAndAvatarDTO;
import com.ldm.pojo.Comment;
import com.ldm.repositories.CommentRepository;
import java.util.ArrayList;
import java.util.Date;
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
public class CommentRepositoryImpl implements CommentRepository {

    public static final int PAGE_SIZE = 6;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Comment addOrUpdate(Comment comment) {
        Session s = this.factory.getObject().getCurrentSession();
        if (comment.getId() == null) {
            s.persist(comment);
        } else {
            s.merge(comment);
        }

        return comment;
    }

//    @Override
//    public List<CommentInfoDTO> getCommentsByForumPostId(Integer forumPostId) {
//        Session session = this.factory.getObject().getCurrentSession();
//
//        Query<Object[]> query = session.createQuery(
//                "SELECT c.id, fp.id, u.id, u.firstName, u.lastName, u.avatar, c.content, c.createdAt "
//                + "FROM Comment c JOIN c.forumPostId fp JOIN c.userId u "
//                + "WHERE fp.id = :forumPostId",
//                Object[].class
//        );
//        query.setParameter("forumPostId", forumPostId);
//
//        List<Object[]> results = query.getResultList();
//        List<CommentInfoDTO> dtos = new ArrayList<>();
//
//        for (Object[] row : results) {
//            UserNameAndAvatarDTO userDTO = new UserNameAndAvatarDTO(
//                    (int) row[2],
//                    (String) row[3],
//                    (String) row[4],
//                    (String) row[5]
//            );
//
//            CommentInfoDTO commentDTO = new CommentInfoDTO(
//                    (Integer) row[0], // comment id
//                    (Integer) row[1], // forum post id
//                    userDTO,
//                    (String) row[6], // content
//                    (Date) row[7] // createdDate
//            );
//
//            dtos.add(commentDTO);
//        }
//
//        return dtos;
//    }
    @Override
    public List<CommentInfoDTO> getCommentsByForumPostId(Integer forumPostId, int page) {
        Session session = this.factory.getObject().getCurrentSession();

        Query<Object[]> query = session.createQuery(
                "SELECT c.id, fp.id, u.id, u.firstName, u.lastName, u.avatar, c.content, c.createdAt "
                + "FROM Comment c JOIN c.forumPostId fp JOIN c.userId u "
                + "WHERE fp.id = :forumPostId ORDER BY c.id DESC",
                Object[].class
        );
        query.setParameter("forumPostId", forumPostId);

        // Phân trang
        int offset = (page - 1) * PAGE_SIZE;
        query.setFirstResult(offset);
        query.setMaxResults(PAGE_SIZE);

        List<Object[]> results = query.getResultList();
        List<CommentInfoDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            UserNameAndAvatarDTO userDTO = new UserNameAndAvatarDTO(
                    (int) row[2],
                    (String) row[3],
                    (String) row[4],
                    (String) row[5]
            );

            CommentInfoDTO commentDTO = new CommentInfoDTO(
                    (Integer) row[0], // comment id
                    (Integer) row[1], // forum post id
                    userDTO,
                    (String) row[6], // content
                    (Date) row[7] // createdDate
            );

            dtos.add(commentDTO);
        }

        return dtos;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.dto.CommentInfoDTO;
import com.ldm.pojo.Comment;
import com.ldm.repositories.CommentRepository;
import com.ldm.repositories.CourseRepository;
import com.ldm.services.CommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    
    @Override
    public Comment addOrUpdate(Comment comment) {
        if(comment.getContent()==null)
            throw new IllegalArgumentException("Chưa có nội dung");
        return this.commentRepository.addOrUpdate(comment);
    }

    @Override
    public List<CommentInfoDTO> getCommentsByForumPostId(Integer forumPostId) {
        return this.commentRepository.getCommentsByForumPostId(forumPostId);
    }
    
}

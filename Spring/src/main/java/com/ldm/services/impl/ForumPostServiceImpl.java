/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.dto.ForumPostInfoDTO;
import com.ldm.pojo.ForumPost;
import com.ldm.repositories.ForumPostRepository;
import com.ldm.services.ForumPostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class ForumPostServiceImpl implements ForumPostService{
    @Autowired
    private ForumPostRepository forumPostRepository;

    @Override
    public ForumPost addOrUpdate(ForumPost forumPost) {
        if(forumPost.getContent()==null)
            throw new IllegalArgumentException("Chưa có nội dung");
        if(forumPost.getTitle()==null)
            throw new IllegalArgumentException("Chưa có tiêu đề");
        return forumPostRepository.addOrUpdate(forumPost);
    }

    @Override
    public List<ForumPost> getForumPostByCourseSession(Integer courseSessionId) {
        return forumPostRepository.getForumPostByCourseSession(courseSessionId);
    }

    @Override
    public List<ForumPostInfoDTO> getForumPostDTOsByCourseSession(Integer courseSessionId) {
        return forumPostRepository.getForumPostDTOsByCourseSession(courseSessionId);
    }

    @Override
    public Integer getCourseSessionIdByForumPostId(Integer forumPostId) {
        return forumPostRepository.getCourseSessionIdByForumPostId(forumPostId);
    }
    
}

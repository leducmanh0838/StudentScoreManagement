/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.dto.ForumPostInfoDTO;
import com.ldm.pojo.ForumPost;
import com.ldm.repositories.ForumPostRepository;
import com.ldm.services.ForumPostService;
import com.ldm.utils.HtmlSanitizerUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class ForumPostServiceImpl implements ForumPostService {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Override
    public ForumPost addOrUpdate(ForumPost forumPost) {
        if (forumPost.getContent() == null) {
            throw new IllegalArgumentException("Chưa có nội dung");
        }
        if (forumPost.getTitle() == null) {
            throw new IllegalArgumentException("Chưa có tiêu đề");
        }

        String originalContent = forumPost.getContent();
        String sanitizedContent = HtmlSanitizerUtil.sanitize(originalContent);

        if (sanitizedContent.length() < originalContent.length()) {
            throw new IllegalArgumentException("Nội dung chứa mã độc.");
        }

        return forumPostRepository.addOrUpdate(forumPost);
    }

    @Override
    public List<ForumPost> getForumPostByCourseSession(Integer courseSessionId) {
        return forumPostRepository.getForumPostByCourseSession(courseSessionId);
    }

    @Override
    public List<ForumPostInfoDTO> getForumPostDTOsByCourseSession(Integer courseSessionId, int page) {
        return forumPostRepository.getForumPostDTOsByCourseSession(courseSessionId, page);
    }

    @Override
    public Integer getCourseSessionIdByForumPostId(Integer forumPostId) {
        return forumPostRepository.getCourseSessionIdByForumPostId(forumPostId);
    }

}

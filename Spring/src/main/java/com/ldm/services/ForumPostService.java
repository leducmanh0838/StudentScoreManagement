/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services;

import com.ldm.dto.ForumPostInfoDTO;
import com.ldm.pojo.ForumPost;
import java.util.List;

/**
 *
 * @author PC
 */
public interface ForumPostService {
    ForumPost addOrUpdate(ForumPost forumPost);
    List<ForumPost> getForumPostByCourseSession(Integer courseSessionId);
    List<ForumPostInfoDTO> getForumPostDTOsByCourseSession(Integer courseSessionId);
}

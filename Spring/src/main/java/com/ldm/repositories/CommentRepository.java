/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories;

import com.ldm.dto.CommentInfoDTO;
import com.ldm.pojo.Comment;
import java.util.List;

/**
 *
 * @author PC
 */
public interface CommentRepository {
    Comment addOrUpdate(Comment comment);
    List<CommentInfoDTO> getCommentsByForumPostId(Integer forumPostId, int page);
}

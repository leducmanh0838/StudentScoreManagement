/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.dto;

import com.ldm.pojo.Comment;

/**
 *
 * @author PC
 */
public class CommentDTO {
    private Integer id;
    private Integer forumPostId;
    private Integer userId;
    private String content;

    public CommentDTO(Integer id, Integer forumPostId, Integer userId, String content) {
        this.id = id;
        this.forumPostId = forumPostId;
        this.userId = userId;
        this.content = content;
    }
    
    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.forumPostId = comment.getForumPostId().getId();
        this.userId = comment.getUserId().getId();
        this.content = comment.getContent();
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the forumPostId
     */
    public Integer getForumPostId() {
        return forumPostId;
    }

    /**
     * @param forumPostId the forumPostId to set
     */
    public void setForumPostId(Integer forumPostId) {
        this.forumPostId = forumPostId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    
}

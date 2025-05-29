/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.dto;

import java.util.Date;

/**
 *
 * @author PC
 */
public class CommentInfoDTO {
    private Integer id;
    private Integer forumPostId;
    private UserNameAndAvatarDTO user;
    private String content;
    private Date createdDate;

    public CommentInfoDTO(Integer id, Integer forumPostId, UserNameAndAvatarDTO user, String content, Date createdDate) {
        this.id = id;
        this.forumPostId = forumPostId;
        this.user = user;
        this.content = content;
        this.createdDate=createdDate;
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
     * @return the user
     */
    public UserNameAndAvatarDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserNameAndAvatarDTO user) {
        this.user = user;
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

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    
}

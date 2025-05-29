/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.dto;

import com.ldm.pojo.ForumPost;
import java.util.Date;

/**
 *
 * @author PC
 */
public class ForumPostDTO {
    private Integer id;
    private Integer user;
    private Integer courseSessionId;
    private String title;
    private String content;

    public ForumPostDTO(Integer id, Integer user, Integer courseSessionId, String title, String content) {
        this.id = id;
        this.user = user;
        this.courseSessionId = courseSessionId;
        this.title = title;
        this.content = content;
    }

    public ForumPostDTO(ForumPost forumPost) {
        this.id = forumPost.getId();
        this.user = forumPost.getUserId().getId();
        this.courseSessionId = forumPost.getCourseSessionId().getId();
        this.title = forumPost.getTitle();
        this.content = forumPost.getContent();
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
     * @return the courseSessionId
     */
    public Integer getCourseSessionId() {
        return courseSessionId;
    }

    /**
     * @param courseSessionId the courseSessionId to set
     */
    public void setCourseSessionId(Integer courseSessionId) {
        this.courseSessionId = courseSessionId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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
     * @return the user
     */
    public Integer getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(Integer user) {
        this.user = user;
    }  
}

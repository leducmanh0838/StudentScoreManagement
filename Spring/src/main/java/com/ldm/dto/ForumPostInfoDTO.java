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
public class ForumPostInfoDTO {
    private Integer id;
    private UserNameAndAvatarDTO user;
    private Integer courseSessionId;
    private String title;
    private String content;
    private Date createdDate;

    public ForumPostInfoDTO(Integer id, UserNameAndAvatarDTO user, Integer courseSessionId, String title, String content, Date createdDate) {
        this.id = id;
        this.user = user;
        this.courseSessionId = courseSessionId;
        this.title = title;
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

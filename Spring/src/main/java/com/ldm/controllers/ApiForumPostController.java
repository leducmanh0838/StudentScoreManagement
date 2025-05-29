/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.configs.LoggerConfig;
import com.ldm.dto.CommentDTO;
import com.ldm.dto.CommentInfoDTO;
import com.ldm.dto.ForumPostDTO;
import com.ldm.dto.ForumPostInfoDTO;
import com.ldm.pojo.Comment;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.ForumPost;
import com.ldm.pojo.User;
import com.ldm.services.CommentService;
import com.ldm.services.CourseService;
import com.ldm.services.CourseSessionService;
import com.ldm.services.ForumPostService;
import com.ldm.services.GradeService;
import com.ldm.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiForumPostController {

    @Autowired
    private CourseSessionService courseSessionService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private UserService userService;
    @Autowired
    private ForumPostService forumPostService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/secure/forumPost/{forumPostId}/addComment")
    public ResponseEntity<?> addComment(
            @PathVariable(name = "forumPostId") Integer forumPostId,
            HttpServletRequest request,
            @RequestBody Comment input) {
        Integer userId = ((Number) request.getAttribute("id")).intValue();
        String role = request.getAttribute("role").toString();
        Integer courseSessionId = forumPostService.getCourseSessionIdByForumPostId(forumPostId);
        
        if(role.equals(User.STUDENT_ROLE)) {
            if (!this.courseSessionService.isStudentEnrolledInCourseSession(courseSessionId, userId)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Bạn không phải là sinh viên môn này!"
                );
            }
        } else if (role.equals(User.TEACHER_ROLE)) {
            if (!this.courseSessionService.isTeacherOwnerOfCourseSession(courseSessionId, userId)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Bạn không phải là giảng viên môn này!"
                );
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không phải là giảng viên hoặc sinh viên!"
            );
        }
        
        input.setForumPostId(new ForumPost(forumPostId));
        input.setUserId(new User(userId));
        
        Comment comment = commentService.addOrUpdate(input);
        
        return ResponseEntity.ok(new CommentDTO(comment));
    }
    
    @GetMapping("/secure/forumPost/{forumPostId}/getComments")
    public ResponseEntity<?> getCommentsByForumPost(
            @PathVariable(name = "forumPostId") Integer forumPostId) {
//        List<ForumPost> forumPosts = forumPostService.getForumPostByCourseSession(courseSessionId);
        List<CommentInfoDTO> commentInfoDTOs = commentService.getCommentsByForumPostId(forumPostId);

        return ResponseEntity.ok(commentInfoDTOs);
    }
}

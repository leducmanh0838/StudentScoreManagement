/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.pojo.User;
import com.ldm.repositories.impl.UserRepositoryImpl;
import com.ldm.services.UserService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author PC
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/userListView")
    public String userListView(Model model, @RequestParam Map<String, String> params) {
        
        List<User> users = this.userService.getUsersForStaff(params);
        model.addAttribute("users", users);
        model.addAttribute("currentRole", params.get("role"));
        model.addAttribute("isLoadMore", users.size()==UserRepositoryImpl.PAGE_SIZE);
    //        model.addAttribute("debugMessage", "Số lượng users trả về: " + users.size());
        
        if(params.get("page")!=null)
            model.addAttribute("currentPage", Integer.valueOf(params.get("page")));
        else
            model.addAttribute("currentPage", 1);
        return "userListView";
    }
    
    
    @GetMapping("/addOrUpdateTeacherView")
    public String addTeacherView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("user", new User());
        return "addOrUpdateTeacher";
    }
    
    @GetMapping("/addOrUpdateTeacherView/{user_id}")
    public String updateTeacherView(Model model, @PathVariable(value="user_id") int id) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "addOrUpdateTeacher";
    }
    
    @PostMapping("/addOrUpdateTeacher")
    public String addOrUpdateTeacher(@ModelAttribute(value = "user") User u){
        this.userService.addOrUpdateTeacher(u);
        return "redirect:/userListView";
    }
}

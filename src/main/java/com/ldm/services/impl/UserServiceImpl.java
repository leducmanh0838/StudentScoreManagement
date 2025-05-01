/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ldm.pojo.User;
import com.ldm.repositories.UserRepository;
import com.ldm.repositories.impl.UserRepositoryImpl;
import com.ldm.services.UserService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinary;
    
    @Override
    public List<User> getAllUsers() {
        return this.userRepository.getAllUsers();
    }

    @Override
    public List<User> getUsersForStaff(Map<String, String> params) {
        return this.userRepository.getUsersForStaff(params);
    }

    @Override
    public User addOrUpdateTeacher(User teacher) {
        if(!teacher.getFile().isEmpty()){
            try {
                Map res = cloudinary.uploader().upload(teacher.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                teacher.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.userRepository.addOrUpdateTeacher(teacher);
    }

    @Override
    public User getUserById(int id) {
        return this.userRepository.getUserById(id);
    }
    
}

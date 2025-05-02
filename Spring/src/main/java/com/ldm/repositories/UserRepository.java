/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.repositories;

import com.ldm.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC
 */
public interface UserRepository {
    List<User> getAllUsers();
    List<User> getUsersForStaff(Map<String, String> params);
    User addOrUpdateTeacher(User teacher);
    User getUserById(int id);
    User addUser(User u);
    User getUserByEmail(String email);
    boolean authenticate(String email, String password);
}

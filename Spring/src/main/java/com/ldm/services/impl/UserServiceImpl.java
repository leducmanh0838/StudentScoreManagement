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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PC
 */
@Service("userDetailsService")
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
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
        
        if(teacher.getPassword()!=null&&!teacher.getPassword().isEmpty()){
            String hashed = passwordEncoder.encode(teacher.getPassword());
            teacher.setPassword(hashed);
        }
        return this.userRepository.addOrUpdateTeacher(teacher);
    }

    @Override
    public User getUserById(int id) {
        return this.userRepository.getUserById(id);
    }
    
    @Override
    public User addUser(Map<String, String> params, MultipartFile avatar) {
        String firstName = params.get("firstName");
        String lastName = params.get("lastName");
        String email = params.get("email");
        String password = params.get("password");
        String userCode = params.get("userCode");

        // Kiểm tra rỗng
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("Thiếu firstName");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Thiếu lastName");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Thiếu email");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Thiếu password");
        if (userCode == null || userCode.isBlank())
            throw new IllegalArgumentException("Thiếu userCode");
        
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPassword(this.passwordEncoder.encode(password));
        u.setUserCode(userCode);
        u.setRole(User.STUDENT_ROLE);
        
        if (avatar!=null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return this.userRepository.addUser(u);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = this.getUserByEmail(email);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));
        
        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(), authorities);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.getUserByEmail(email);
    }

    @Override
    public boolean authenticate(String email, String password) {
        return this.userRepository.authenticate(email, password);
    }
    
}

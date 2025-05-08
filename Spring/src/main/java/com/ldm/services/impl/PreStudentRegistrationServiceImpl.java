/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ldm.pojo.PreStudentRegistration;
import com.ldm.repositories.PreStudentRegistrationRepository;
import com.ldm.services.PreStudentRegistrationService;
import com.ldm.utils.OtpUtil;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PC
 */
@Service
public class PreStudentRegistrationServiceImpl implements PreStudentRegistrationService{
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PreStudentRegistrationRepository preStudentRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private Cloudinary cloudinary;
    
    @Override
    public PreStudentRegistration register(Map<String, String> params, MultipartFile avatar) {
        String firstName = params.get("firstName");
        String lastName = params.get("lastName");
        String email = params.get("email");
        String password = params.get("password");
        
        PreStudentRegistration entity = new PreStudentRegistration();
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
//        entity.setAvatar(avatar);
        entity.setEmail(email);
        String hashed = passwordEncoder.encode(password);
        entity.setPassword(hashed);
        String otp = OtpUtil.generateSecureOTP();
        entity.setOtp(otp);
        LocalDateTime ldt = LocalDateTime.now().plusMinutes(15);
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        entity.setOtpExpiration(date);
        
        if (avatar!=null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                entity.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        emailService.sendVerificationEmail(email, otp);
        
        return this.preStudentRepo.addOrUpdate(entity);
    }
    
}

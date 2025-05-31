/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services.impl;

import com.ldm.services.EmailService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ldm.pojo.PreStudentRegistration;
import com.ldm.pojo.User;
import com.ldm.repositories.PreStudentRegistrationRepository;
import com.ldm.repositories.UserRepository;
import com.ldm.services.PreStudentRegistrationService;
import com.ldm.utils.MyUserUtil;
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
    private static int MAX_ATTEMPTS=3;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PreStudentRegistrationRepository preStudentRepo;
    @Autowired
    private UserRepository userRepo;
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
        
        if (firstName == null || firstName.isEmpty())
            throw new IllegalArgumentException("Thiếu firstName");
        if (lastName == null || lastName.isEmpty())
            throw new IllegalArgumentException("Thiếu lastName");
        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("Thiếu email");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Thiếu password");
        
        if(!MyUserUtil.isStrongPassword(password))
            throw new IllegalArgumentException("mật khẩu yếu");
        if(!MyUserUtil.isOuEmail(email))
            throw new IllegalArgumentException("sử dụng đúng email trường");
        if(this.userRepo.isEmailExists(email))
            throw new IllegalArgumentException("email đã tồn tại");
        
        
        PreStudentRegistration entity = new PreStudentRegistration();
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
//        entity.setAvatar(avatar);
        entity.setEmail(email);
        String hashed = passwordEncoder.encode(password);
        entity.setPassword(hashed);
        String otp = MyUserUtil.generateSecureOTP();
        entity.setOtp(otp);
        LocalDateTime ldt = LocalDateTime.now().plusMinutes(15);
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        entity.setOtpExpiration(date);
        entity.setVerificationAttempts(0);
        
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

    @Override
    public boolean verifyOtp(String email, String otp) {
        PreStudentRegistration preStudent = preStudentRepo.findByEmail(email);

        if (preStudent == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng.");
        }

        // thời gian hết hạn
        Date otpExpiration = preStudent.getOtpExpiration();
        LocalDateTime expirationTime = otpExpiration.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();

        if (expirationTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP đã hết hạn.");
        }

        if (!preStudent.getOtp().equals(otp)) {
            int attempts = preStudent.getVerificationAttempts() + 1;
            preStudent.setVerificationAttempts(attempts);

            if (attempts >= MAX_ATTEMPTS) {
                // quá số lần
                preStudentRepo.delete(preStudent);
                throw new IllegalArgumentException(String.format("Bạn đã nhập sai OTP quá %s lần. Tài khoản đã bị xóa.", MAX_ATTEMPTS));
            } else {
                preStudentRepo.addOrUpdate(preStudent);
                throw new IllegalArgumentException("OTP không đúng. Bạn còn " + (MAX_ATTEMPTS - attempts) + " lần thử.");
            }
        }
        
        User newStudent = new User();
        newStudent.setUserCode(MyUserUtil.getUserCode(email));
        newStudent.setFirstName(preStudent.getFirstName());
        newStudent.setLastName(preStudent.getLastName());
        newStudent.setEmail(email);
        newStudent.setPassword(preStudent.getPassword());
        newStudent.setRole(User.STUDENT_ROLE);
        newStudent.setAvatar(preStudent.getAvatar());
        
        userRepo.addOrUpdateUser(newStudent);
        
        preStudentRepo.delete(preStudent);

//        preStudent.setVerificationAttempts(0);
//        preStudent.setOtp(null);
//        preStudent.setOtpExpiration(null);
//        preStudentRepository.addOrUpdate(preStudent);
        return true;
    }
    
}

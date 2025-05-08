/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.services;

import com.ldm.pojo.PreStudentRegistration;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PC
 */
public interface PreStudentRegistrationService {
    PreStudentRegistration register(Map<String, String> params, MultipartFile avatar);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.components.MessageHelper;
import com.ldm.pojo.User;
import com.ldm.repositories.impl.UserRepositoryImpl;
import com.ldm.services.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
public class IndexController {
    
    private static final List<String> MESSAGE_KEYS = List.of(
            "USER_LIST" // có thể truyền thêm từ controller nếu muốn tùy biến
    );
    
    @Autowired
    private MessageHelper messageHelper;

    @Autowired
    private UserService userService;
    
    @RequestMapping("/")
    public String index(Model model, @RequestParam Map<String, String> params) {
//        String lang = params.getOrDefault("lang", "vi");
//        Map<String, String> messages = messageHelper.getMessages(lang, MESSAGE_KEYS);
//        model.addAttribute("users", this.userService.getAllUsers());
//        model.addAttribute("messages", messages);
        return "index";
    }
        
        
//       @Autowired
//    private Map<String, MessageSource> customMessageSources;
//
//    private static final List<String> NEEDED_KEYS = List.of(
//            "USER_LIST", "USER_NAME" // thêm các key bạn cần
//    );
//
//    @GetMapping("/")
//    public String home(Model model, @RequestParam Map<String, String> params) {
//        MessageSource ms = customMessageSources.getOrDefault(params.get("lang"), customMessageSources.get("vi"));
//
//        Map<String, String> messages = new HashMap<>();
//        for (String key : NEEDED_KEYS) {
//            String value = ms.getMessage(key, null, Locale.ROOT);
//            messages.put(key, value);
//        }
//        model.addAttribute("messages", messages); // truyền 1 object chứa các message cần thiết
//        return "index";
//    }
        
        
        
//        private static final List<String> NEEDED_KEYS = List.of(
//            "USER_LIST", "USER_NAME" // có thể truyền thêm từ controller nếu muốn tùy biến
//    );
//
//    @Autowired
//    private MessageHelper messageHelper;
//
//    @GetMapping("/")
//    public String home(Model model, @RequestParam Map<String, String> params) {
//        String lang = params.getOrDefault("lang", "vi");
//        Map<String, String> messages = messageHelper.getMessages(lang, NEEDED_KEYS);
//        model.addAttribute("messages", messages);
//        return "index";
//    }
}

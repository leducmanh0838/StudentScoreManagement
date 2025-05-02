///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.ldm.utils;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//
///**
// *
// * @author PC
// */
//public class MessageUtil {
//    
//    @Autowired
//    private Map<String, MessageSource> customMessageSources;
//    
//    public static Map<String, String> getMessages(String lang, List<String> keys){
//        MessageSource ms = customMessageSources.getOrDefault(lang, customMessageSources.get("vi"));
//        Map<String, String> messages = new HashMap<>();
//        for (String key : keys) {
//            messages.put(key, ms.getMessage(key, null, Locale.ROOT));
//        }
//        return messages;
//    }
//}

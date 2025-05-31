/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author PC
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("gradesByCourseSession");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("gradesByCourseSession");
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.MINUTES) // 30 phút
                        .maximumSize(1000)
        );
        return cacheManager;
    }
}

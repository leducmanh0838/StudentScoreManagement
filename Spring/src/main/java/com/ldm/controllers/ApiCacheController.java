/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api/cache")
@CrossOrigin
public class ApiCacheController {

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/{cacheName}")
    public ResponseEntity<?> viewCache(@PathVariable(name = "cacheName") String cacheName) {
        org.springframework.cache.Cache springCache = cacheManager.getCache(cacheName);
        if (springCache == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cache not found: " + cacheName);
        }

        Object nativeCache = springCache.getNativeCache();

        // Nếu là Caffeine
        if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache<?, ?> caffeineCache) {
            Map<Object, Object> mapView = (Map<Object, Object>) caffeineCache.asMap();
            List<Map<String, Object>> entries = new ArrayList<>();
            for (Map.Entry<?, ?> entry : mapView.entrySet()) {
                Map<String, Object> entryMap = new HashMap<>();
                entryMap.put("key", entry.getKey());
                entryMap.put("value", entry.getValue());
                entries.add(entryMap);
            }
            return ResponseEntity.ok(entries);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unsupported cache implementation: " + nativeCache.getClass());
    }
}

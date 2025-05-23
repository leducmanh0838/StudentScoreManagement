/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.filters;

import com.ldm.utils.JwtUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author huu-thanhduong
 */
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String securePath = String.format("%s/api/secure", contextPath);

        // Chỉ áp dụng lọc cho các đường dẫn bắt đầu bằng /api/secure
        if (path.startsWith(securePath)) {
            String header = httpRequest.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
                return;
            }

            String token = header.substring(7);

            try {
                Map<String, Object> userDetails = JwtUtils.validateTokenAndGetUserDetails(token);
                if (userDetails != null) {
                    String role = (String) userDetails.get("role");

                    // Nếu truy cập endpoint dành riêng cho giáo viên thì kiểm tra role
                    if (path.startsWith(securePath + "/teacherAuth")) {
                        if (!"teacher".equals(role)) {
                            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập endpoint dành cho giáo viên.");
                            return;
                        }
                    }

                    // Gắn attribute vào request
                    httpRequest.setAttribute("id", userDetails.get("id"));
                    httpRequest.setAttribute("username", userDetails.get("username"));
                    httpRequest.setAttribute("role", role);

                    UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(userDetails.get("username"), null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    chain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                // Có thể log lỗi ở đây nếu cần
            }

            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc đã hết hạn.");
            return;
        }

        chain.doFilter(request, response);
    }

}

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

        if (httpRequest.getRequestURI().startsWith(String.format("%s/api/secure", httpRequest.getContextPath())) == true) {

            String header = httpRequest.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
                return;
            } else {
                String token = header.substring(7);
                try {
                    Map<String, Object> userDetails = JwtUtils.validateTokenAndGetUserDetails(token);
                    if (userDetails != null) {
                        // Truy xuất thông tin từ Map
                        Long id = (Long) userDetails.get("id");
                        String username = (String) userDetails.get("username");
                        String role = (String) userDetails.get("role");

                        // Đặt thông tin vào request attributes để có thể sử dụng trong các xử lý sau
                        httpRequest.setAttribute("id", id);
                        httpRequest.setAttribute("username", username);
                        httpRequest.setAttribute("role", role);

                        // Tạo đối tượng authentication với thông tin user (chưa có password và authorities)
                        UsernamePasswordAuthenticationToken authentication
                                = new UsernamePasswordAuthenticationToken(username, null, null); // Có thể thêm authorities vào đây nếu cần
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // Tiến hành tiếp tục chuỗi bộ lọc
                        chain.doFilter(request, response);
                        return;
                    }
                } catch (Exception e) {
                    // Log lỗi
                }
            }

            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Token không hợp lệ hoặc hết hạn");
        }

        chain.doFilter(request, response);
    }

}

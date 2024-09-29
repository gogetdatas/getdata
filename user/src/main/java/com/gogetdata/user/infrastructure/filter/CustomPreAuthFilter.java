package com.gogetdata.user.infrastructure.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class CustomPreAuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-Role");
        String companyId = request.getHeader("X-Company_Id");
        if (userId != null && role != null && companyId != null) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            CustomUserDetails userDetails = new CustomUserDetails(
                    Long.parseLong(userId),
                    Long.parseLong(companyId),
                    Collections.singletonList(authority)
            );

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
            return;
        }
        filterChain.doFilter(request, response);
    }
}

package com.gogetdata.channel.infrastructrue.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Configuration
public class FeignClientConfig {
    @Bean
    public RequestInterceptor customFeignRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 현재 스레드의 요청 컨텍스트 가져오기
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                if (requestAttributes == null) {
                    return;
                }

                if (requestAttributes instanceof ServletRequestAttributes) {
                    HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

                    // 필요한 헤더 추출
                    String userId = request.getHeader("X-User-Id");
                    String role = request.getHeader("X-User-Type");
                    String companyId = request.getHeader("X-Company-Id");
                    String companyType = request.getHeader("X-Company-Type");

                    // 헤더가 존재하면 Feign 요청에 추가
                    if (userId != null) {
                        template.header("X-User-Id", userId);
                    }
                    if (role != null) {
                        template.header("X-User-Type", role);
                    }
                    if (companyId != null) {
                        template.header("X-Company-Id", companyId);
                    }
                    if (companyType != null) {
                        template.header("X-Company-Type", companyType);
                    }
                }
            }
        };
    }
}

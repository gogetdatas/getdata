package com.gogetdata.gateway;

import com.gogetdata.gateway.application.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
@Component
public class JwtAuthenticationFilter implements GlobalFilter {
    private final String secretKey;

    private final AuthService authService;

    // FeignClient 와 Global Filter 의 순환참조 문제가 발생하여 Bean 초기 로딩 시 순환을 막기 위해 @Lazy 어노테이션을 추가함.
    public JwtAuthenticationFilter(@Value("${service.jwt.secret-key}") String secretKey, @Lazy AuthService authService) {
        this.secretKey = secretKey;
        this.authService = authService;
    }

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        // /auth 로 시작하는 요청들은 검증하지 않습니다.
        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);
        Jws<Claims> payload = getPayLoad(token);

        // 토큰이 존재하지 않거나, validateToken(token) 기준에 부합하지 않으면 401 에러를 응답합니다.
        if (token == null || !validateToken(payload)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        exchange.getRequest().mutate()
                .header("X-User-Id", payload.getPayload().get("user_id").toString())
                .header("X-User-Role", payload.getPayload().get("user_role").toString())
                .header("X-Company-Id", payload.getPayload().get("company_id").toString())
                .header("X-Company-Role", payload.getPayload().get("company_role").toString())
                .build();
        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        // Request Header 에서 Authorization Key 로 설정된 된 값을 불러옵니다.
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        // 값이 존재하며, Bearer {token} 형태로 시작할 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 값중 앞부분 ('Bearer ') 을 제거하고 응답합니다.
            return authHeader.substring(7);
        }
        return null;
    }
    private Jws<Claims> getPayLoad(String token){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        // JWT 에 설정된 정보를 불러옵니다.
        return  Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(token);
    }

    private boolean validateToken(Jws<Claims> claimsJws) {
        try {
            // JWT 값 중 Payload 부분에 user_id 로 설정된 값이 있는 경우
            if (claimsJws.getPayload().get("user_id") != null) {
                // user_id 추출 로직
                String userId = claimsJws.getPayload().get("user_id").toString();
                // user_id 값으로 해당 유저가 회원가입 한 유저인지 인증 서비스를 통해 확인합니다.
                return authService.verifyUser(userId);
            } else {
                return false;
            }
        }catch (Exception e) {
            return false;
        }
    }
}

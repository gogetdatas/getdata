package com.gogetdata.auth.application;

import com.gogetdata.auth.application.dto.AuthResponse;
import com.gogetdata.auth.application.dto.signInRequest;
import com.gogetdata.auth.application.dto.signUpRequest;
import com.gogetdata.auth.domain.entity.User;
import com.gogetdata.auth.domain.entity.UserTypeEnum;
import com.gogetdata.auth.domain.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;
    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public static final String BEARER_PREFIX = "Bearer ";

    public AuthService(UserRepository userRepository,
                       @Value("${service.jwt.secret-key}") String secretKey , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void signUp(signUpRequest signUpRequest) {
        existEmail(signUpRequest.getEmail());
        User user = User.create(signUpRequest.getUserName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                UserTypeEnum.USER);
        userRepository.save(user);
    }
    private void existEmail(String email){
        Optional<User> userEmail = userRepository.findByemail(email);
        if(userEmail.isPresent()){
            throw new IllegalArgumentException("해당 이메일 존재함");
        }
    }
    public AuthResponse signIn(signInRequest signInRequest) {
        User user = findByEmail(signInRequest.getEmail());
        if(!passwordEncoder.matches(signInRequest.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 다름");
        }
        return createToken(user.getUserId(),user.getUserType(),user.getCompanyId(),user.getCompanyType());
    }
    public AuthResponse createToken(Long userId, UserTypeEnum role , Long companyId, String companyRole) {
        return AuthResponse.of(BEARER_PREFIX + Jwts.builder()
                .claim("user_id", userId)
                .claim("user_role",role)
                .claim("company_id",companyId == null ? "null":companyId)
                .claim("company_role",companyRole == null ? "null":companyRole)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact());
    }
    public Boolean verifyUser(final Long userId) {
        return findById(userId) != null;
    }
    public User findByEmail(String email){
        return userRepository.findByemail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지않음"));
    }
    @Cacheable(value = "usersById", key = "#userId")
    public User findById(Long userId){
        return userRepository.findById(userId)
                .filter(o -> !o.isDeleted())
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지않음")
                );
    }
}

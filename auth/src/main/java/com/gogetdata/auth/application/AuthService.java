package com.gogetdata.auth.application;

import com.gogetdata.auth.application.dto.AuthResponse;
import com.gogetdata.auth.application.dto.signInRequest;
import com.gogetdata.auth.application.dto.signUpRequest;
import com.gogetdata.auth.domain.entity.User;
import com.gogetdata.auth.domain.entity.UserInfo;
import com.gogetdata.auth.domain.entity.UserTypeEnum;
import com.gogetdata.auth.domain.repository.UserInfoRepository;
import com.gogetdata.auth.domain.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    public static final String BEARER_PREFIX = "Bearer ";

    public AuthService(UserRepository userRepository,
                       @Value("${service.jwt.secret-key}") String secretKey , PasswordEncoder passwordEncoder,UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
    }
    @Transactional
    public void signUp(signUpRequest signUpRequest) {
        existEmail(signUpRequest.getEmail());
        User user = User.create(signUpRequest.getUserName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                UserTypeEnum.USER);
        userRepository.save(user);
        userInfoRepository.save(UserInfo.create(user.getUserId(), String.valueOf(user.getUserType())));
    }
    private void existEmail(String email){
        Optional<User> userEmail = userRepository.findByemail(email);
        if(userEmail.isPresent()){
            throw new IllegalArgumentException("해당 이메일 존재함");
        }
    }

    public AuthResponse signIn(signInRequest signInRequest) {
        User user =userRepository.findByemail(signInRequest.getEmail())
                .orElseThrow(()->new IllegalArgumentException("Invalid email"));
        if(!passwordEncoder.matches(signInRequest.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 다름");
        }
        UserInfo userInfo = userInfoRepository.findByUserId(user.getUserId());
        return createToken(userInfo.getUserId(),userInfo.getUserType(),userInfo.getCompanyId(),userInfo.getCompanyType());
    }
    public AuthResponse createToken(Long userId, String role , Long companyId,String companyRole) {
        return AuthResponse.of(BEARER_PREFIX + Jwts.builder()
                .claim("user_id", userId)
                .claim("user_role",role)
                .claim("company_id",companyId)
                .claim("company_role",companyRole)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact());
    }
    public Boolean verifyUser(final Long userId) {
        return userRepository.findById(userId)
                .map(user -> !user.isDeleted())
                .orElse(false);
    }
}

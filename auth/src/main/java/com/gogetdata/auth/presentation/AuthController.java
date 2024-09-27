package com.gogetdata.auth.presentation;

import com.gogetdata.auth.application.AuthService;
import com.gogetdata.auth.application.dto.signInRequest;
import com.gogetdata.auth.application.dto.signUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody signUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.ok()
                .body("회원가입완료");
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody signInRequest signInRequest) {
        return ResponseEntity.ok()
                .body(authService.signIn(signInRequest));
    }
    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyUser(final @RequestParam(value = "user_id") Long userId) {
        return ResponseEntity.ok()
                .body(authService.verifyUser(userId));
    }
}

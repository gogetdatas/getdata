package com.gogetdata.user.presentation;

import com.gogetdata.user.application.UserService;
import com.gogetdata.user.application.dto.*;
import com.gogetdata.user.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<MyInfoResponse> getMyInfo(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        MyInfoResponse response = userService.readMyInfo(userId, userDetails);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<MyInfoResponse> updateMyInfo(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestBody UpdateMyInfoRequest updateRequest) {
        MyInfoResponse response = userService.updateMyInfo(userId, userDetails, updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        DeleteUserResponse response = userService.deleteUser(userId, userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/company/registers")
    public ResponseEntity<List<RegistrationResults>> registerUsers (@RequestBody List<UserRegistrationDto> userRegistrationDto) {
        List<RegistrationResults> response = userService.registrationUsers(userRegistrationDto);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/company/verify/{userId}")
    public ResponseEntity<Boolean> checkUser (@PathVariable Long userId) {
        Boolean response = userService.checkUser(userId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/company/delete/{userId}")
    public ResponseEntity<Boolean> deleteCompanyUser (@PathVariable Long userId) {
        Boolean response = userService.deleteCompanyUser(userId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/company/register/{userId}")
    public ResponseEntity<RegistrationResult> registerUser (@PathVariable Long userId) {
        RegistrationResult response = userService.registrationUser(userId);
        return ResponseEntity.ok(response);
    }
}

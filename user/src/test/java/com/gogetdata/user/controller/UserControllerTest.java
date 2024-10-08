package com.gogetdata.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogetdata.user.application.UserService;
import com.gogetdata.user.application.dto.DeleteUserResponse;
import com.gogetdata.user.application.dto.MyInfoResponse;
import com.gogetdata.user.application.dto.UpdateMyInfoRequest;
import com.gogetdata.user.infrastructure.config.SecurityConfig;
import com.gogetdata.user.infrastructure.filter.CustomPreAuthFilter;
import com.gogetdata.user.infrastructure.filter.CustomUserDetails;
import com.gogetdata.user.presentation.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = UserController.class)
@Import({CustomPreAuthFilter.class, SecurityConfig.class})

class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private Long userId;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        this.userId = 1L;
    }

    @Nested
    @DisplayName("내정보조회 API 테스트")
    class GetMyInfo {
        @DisplayName("정상적으로 내 정보를 조회할 때")
        @Test
        void successGetMyInfo() throws Exception {
            // given
            MyInfoResponse mockResponse = new MyInfoResponse(userId, "abc", "test@test");
            given(userService.readMyInfo(eq(userId), any(CustomUserDetails.class)))
                    .willReturn(mockResponse);
            // when & then
            mockMvc.perform(get("/user/{userId}", userId)
                            .header("X-User-Id", userId.toString())
                            .header("X-Role", "USER")
                            .header("X-Company_Id", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(userId))
                    .andExpect(jsonPath("$.userName").value("abc"))
                    .andExpect(jsonPath("$.email").value("test@test"));
        }


        @DisplayName("유저가 존재하지 않을 때")
        @Test
        void failGetMyInfoNotFound() throws Exception {
            // given
            given(userService.readMyInfo(eq(userId), any(CustomUserDetails.class)))
                    .willThrow(new NoSuchElementException("No user found with id " + userId));

            // when & then
            mockMvc.perform(get("/user/{userId}", userId)
                            .header("X-User-Id", userId.toString())
                            .header("X-Role", "USER")
                            .header("X-Company_Id", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("No user found with id " + userId));
        }

        @DisplayName("유저가 삭제된 상태일 때")
        @Test
        void failGetMyInfoIsDeleted() throws Exception {
            // given
            given(userService.readMyInfo(eq(userId), any(CustomUserDetails.class)))
                    .willThrow(new NoSuchElementException("User with id " + userId + " is deleted."));

            // when & then
            mockMvc.perform(get("/user/{userId}", userId)
                            .header("X-User-Id", userId.toString())
                            .header("X-Role", "USER")
                            .header("X-Company_Id", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("User with id " + userId + " is deleted."));
        }

        @DisplayName("필수 헤더 정보가 없을 때 접근 권한 오류 반환")
        @Test
        void failAccessDeniedNoHeaders() throws Exception {
            mockMvc.perform(get("/user/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());

        }
    }

    @Nested
    @DisplayName("내정보 수정 API 테스트")
    class UpdateMyInfo {
        @DisplayName("정상적으로 내 정보를 수정할 때")
        @Test
        void successUpdateMyInfo() throws Exception {
            // given
            UpdateMyInfoRequest request = new UpdateMyInfoRequest("abcd");

            MyInfoResponse mockResponse = new MyInfoResponse(userId, "abcd", "test@test.com");

            // 서비스 메서드가 호출되면 mockResponse를 반환하도록 설정
            when(userService.updateMyInfo(eq(userId), any(),any(UpdateMyInfoRequest.class))).thenReturn(mockResponse);

            // when
            mockMvc.perform(put("/user/{userId}", userId)
                            .header("X-User-Id", userId.toString())
                            .header("X-Role", "USER")
                            .header("X-Company_Id", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.userId").value(userId))
                    .andExpect(jsonPath("$.userName").value("abcd"))
                    .andExpect(jsonPath("$.email").value("test@test.com"));

            // 서비스 메서드가 정확히 호출되었는지 검증
        Mockito.verify(userService).updateMyInfo(eq(userId), any(),any(UpdateMyInfoRequest.class));
        }
    }
    @Nested
    @DisplayName("유저 삭제 API 테스트")
    class deleteUser {
        @DisplayName("정상적으로 유저를 삭제할때")
        @Test
        void successDeleteUser() throws Exception {
            // given
            DeleteUserResponse mockResponse = DeleteUserResponse.from("삭제완료");
            when(userService.deleteUser(eq(userId), any())).thenReturn(mockResponse);
            mockMvc.perform(delete("/user/{userId}", userId)
                            .header("X-User-Id", userId.toString())
                            .header("X-Role", "USER")
                            .header("X-Company_Id", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.delete").value("삭제완료"));

            // 서비스 메서드가 정확히 호출되었는지 검증
            Mockito.verify(userService).deleteUser(eq(userId), any());
        }
    }
}
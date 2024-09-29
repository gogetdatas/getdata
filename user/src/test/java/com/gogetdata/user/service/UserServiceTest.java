package com.gogetdata.user.service;

import com.gogetdata.user.application.dto.MyInfoResponse;
import com.gogetdata.user.application.dto.UpdateMyInfoRequest;
import com.gogetdata.user.domain.entity.User;
import com.gogetdata.user.domain.entity.UserTypeEnum;
import com.gogetdata.user.domain.repository.UserRepository;
import com.gogetdata.user.domain.service.UserServiceImpl;
import com.gogetdata.user.infrastructure.filter.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.gogetdata.user.application.dto.DeleteUserResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private Long userId;
    private User user;

    @BeforeEach
    void setUp() {
        this.userId = 1L;
        this.user = User.create("abc", "test@test", "123", UserTypeEnum.USER);
        ReflectionTestUtils.setField(user, "userId", userId);
    }

    @Nested
    @DisplayName("권한체크")
    class CheckAuth {
        @DisplayName("ADMIN 권한체크")
        @Test
        void successAuthAdmin() {
            // given
            CustomUserDetails adminUserDetails = new CustomUserDetails(2L, 0L, Collections.singleton(new SimpleGrantedAuthority("ADMIN")));
            Long targetUserId = 1L;

            // when
            boolean result = userService.checkAuth(targetUserId, adminUserDetails);

            // then
            assertThat(result).isTrue();
        }

        @DisplayName("USER 권한체크")
        @Test
        void successAuthUser() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(userId, 0L, Collections.singleton(new SimpleGrantedAuthority("USER")));

            // when
            boolean result = userService.checkAuth(userId, customUserDetails);

            // then
            assertThat(result).isTrue();
        }

        @DisplayName("USER의 아이디와 UserDetails 아이디가 다를 때")
        @Test
        void failAuthUser() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(2L, 0L, Collections.singleton(new SimpleGrantedAuthority("USER")));

            // when
            Throwable throwable = catchThrowable(() -> userService.checkAuth(userId, customUserDetails));

            // then
            assertThat(throwable).isInstanceOf(AccessDeniedException.class);
        }
    }

    @Nested
    @DisplayName("유저조회")
    class ReadUser {
        @DisplayName("id값으로 유저조회 성공")
        @Test
        void successReadMyInfo() {
            // given
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            User result = userService.readUser(userId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getUserId()).isEqualTo(1L);
            assertThat(result.getUserName()).isEqualTo("abc");
            assertThat(result.getEmail()).isEqualTo("test@test");
            assertThat(result.getCompanyStatus()).isEqualTo(0L);
            assertThat(result.getUserType()).isEqualTo(UserTypeEnum.USER);
        }

        @DisplayName("id값으로 유저를 찾지 못했을 때")
        @Test
        void failReadMyInfo() {
            // given
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when
            Throwable throwable = catchThrowable(() -> userService.readUser(userId));

            // then
            assertThat(throwable).isInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("id값으로 유저를 찾았으나 이미 삭제된 유저일 때")
        @Test
        void failReadMyInfoIsDeleted() {
            // given
            user.delete();  // 유저를 삭제된 상태로 설정
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            CustomUserDetails customUserDetails = new CustomUserDetails(userId, 0L, Collections.singleton(new SimpleGrantedAuthority("USER")));

            // when
            Throwable throwable = catchThrowable(() -> userService.readMyInfo(userId, customUserDetails));

            // then
            assertThat(throwable).isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("is deleted");
        }
    }

    @Nested
    @DisplayName("내정보조회")
    class ReadMyInfo {
        @DisplayName("정상적으로 id값으로 조회할 때")
        @Test
        void successReadMyInfo() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(userId, 0L, Collections.singleton(new SimpleGrantedAuthority("USER")));
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            MyInfoResponse result = userService.readMyInfo(userId, customUserDetails);

            // then
            assertThat(result).isNotNull();
            assertThat(result.userId()).isEqualTo(1L);
            assertThat(result.userName()).isEqualTo("abc");
            assertThat(result.email()).isEqualTo("test@test");
            assertThat(result.companyStatus()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("내정보수정")
    class UpdateMyInfo {
        @DisplayName("성공적으로 내정보 업데이트")
        @Test
        void successUpdate() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(userId, 0L, Collections.singleton(new SimpleGrantedAuthority("USER")));
            UpdateMyInfoRequest updateMyInfo = new UpdateMyInfoRequest("abcd");
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            MyInfoResponse result = userService.updateMyInfo(userId, customUserDetails, updateMyInfo);

            // then
            assertThat(result.userName()).isEqualTo("abcd");
        }
    }

    @Nested
    @DisplayName("유저삭제")
    class DeleteUser {
        @DisplayName("성공적으로 유저 삭제")
        @Test
        void successDelete() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(userId, 0L, Collections.singleton(new SimpleGrantedAuthority("USER")));
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            DeleteUserResponse result = userService.deleteUser(userId, customUserDetails);
            // then
            assertThat(result.getDelete()).isEqualTo("회원탈퇴");
        }
    }
}

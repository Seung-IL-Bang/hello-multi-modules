package io.hello.demo.testmodule.unittest.simplesystem.loginservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private SessionManager sessionManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    void 회원이_존재하지_않을_때_예외_발생() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginService.login("test@test.com", "password");
        });
        assertEquals("회원이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void 패스워드_불일_예외_발생() {
        // given
        User user = new User("test@example.com", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            loginService.login("test@example.com", "wrongpassword");
        });
        assertEquals("패스워드가 일치하지 않습니다.", exception.getMessage());
    }


}
package com.bvp.task.auth;

import com.bvp.task.security.jwt.JwtResponse;
import com.bvp.task.user.dto.LoginRequestDto;
import com.bvp.task.user.dto.UserRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testRegisterUser() {
        UserRequestDto userRequestDto = new UserRequestDto(
                UUID.randomUUID(),
                "Dmytro", "Markevych", LocalDate.now(),
                "+380986232523", "123 Street",
                "example@email.com", "test123q!"
        );

        BindingResult bindingResult = mock(BindingResult.class);
        ResponseEntity expectedResponseEntity = ResponseEntity.ok().build();
        when(authService.registerUser(userRequestDto, bindingResult)).thenReturn(expectedResponseEntity);
        ResponseEntity<?> responseEntity = authController.registerUser(userRequestDto, bindingResult);
        assertEquals(expectedResponseEntity, responseEntity);
    }

    @Test
    public void testLoginUser() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@example.com", "password");
        BindingResult bindingResult = mock(BindingResult.class);
        ResponseEntity expectedResponseEntity =
                ResponseEntity.ok().body(
                        new JwtResponse("access_token", "refresh_token")
                );
        when(authService.authenticateUser(loginRequestDto, bindingResult)).thenReturn(expectedResponseEntity);
        ResponseEntity<?> responseEntity = authController.loginUser(loginRequestDto, bindingResult);
        assertEquals(expectedResponseEntity, responseEntity);
    }
}

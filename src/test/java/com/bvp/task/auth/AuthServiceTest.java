package com.bvp.task.auth;

import com.bvp.task.errorhandling.exceptions.UserAlreadyExistsException;
import com.bvp.task.security.jwt.JwtResponse;
import com.bvp.task.security.jwt.JwtTokenUtils;
import com.bvp.task.user.UserService;
import com.bvp.task.user.dto.LoginRequestDto;
import com.bvp.task.user.dto.UserRequestDto;
import com.bvp.task.user.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testAuthenticateUser() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@example.com", "password");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        when(jwtTokenUtils.generateAccessToken(userDetails)).thenReturn("access_token");
        when(jwtTokenUtils.generateRefreshToken(userDetails)).thenReturn("refresh_token");

        ResponseEntity<?> responseEntity = authService.authenticateUser(loginRequestDto, bindingResult);

        assertEquals(200, responseEntity.getStatusCode().value());
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertEquals("access_token", jwtResponse.getAccessToken());
        assertEquals("refresh_token", jwtResponse.getRefreshToken());
    }

    @Test
    public void testRegisterUser() {
        UserRequestDto userRequestDto = new UserRequestDto(
                UUID.randomUUID(),
                "Dmytro", "Markevych", LocalDate.now(),
                "+380986232523", "123 Street",
                "example@email.com", "test123q!"
        );

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.existsByEmail("example@email.com")).thenReturn(false);
        ResponseEntity<?> expectedResponseEntity = ResponseEntity.ok().build();
        when(userService.register(userRequestDto)).thenReturn((UserResponseDto) expectedResponseEntity.getBody());

        ResponseEntity<?> responseEntity = authService.registerUser(userRequestDto, bindingResult);

        assertEquals(expectedResponseEntity, responseEntity);
    }

    @Test
    public void testRegisterUserWithExistingEmail() {
        UserRequestDto userRequestDto = new UserRequestDto(
                UUID.randomUUID(),
                "Dmytro", "Markevych", LocalDate.now(),
                "+380986232523", "123 Street",
                "example@email.com", "test123q!"
        );

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.existsByEmail("example@email.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser(userRequestDto, bindingResult));
    }
}
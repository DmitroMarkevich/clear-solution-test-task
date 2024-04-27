package com.bvp.task.auth;

import com.bvp.task.errorhandling.ErrorUtils;
import com.bvp.task.errorhandling.exceptions.ItemNotCreatedException;
import com.bvp.task.errorhandling.exceptions.LoginException;
import com.bvp.task.errorhandling.exceptions.UserAlreadyExistsException;
import com.bvp.task.security.jwt.JwtResponse;
import com.bvp.task.security.jwt.JwtTokenUtils;
import com.bvp.task.user.UserService;
import com.bvp.task.user.dto.LoginRequestDto;
import com.bvp.task.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> authenticateUser(LoginRequestDto authenticationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new LoginException(ErrorUtils.handleValidationErrors(bindingResult).toString());
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));

        String email = authenticationRequest.getEmail();

        UserDetails userDetails = userService.loadUserByUsername(email);
        String accessToken = jwtTokenUtils.generateAccessToken(userDetails);
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);

        return ResponseEntity.ok().body(new JwtResponse(accessToken, refreshToken));
    }

    public ResponseEntity<?> registerUser(UserRequestDto registrationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotCreatedException(ErrorUtils.handleValidationErrors(bindingResult).toString());
        }

        if (userService.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException(String.format("User email '%s' already exists", registrationRequest.getEmail()));
        }

        return ResponseEntity.ok().body(userService.register(registrationRequest));
    }
}

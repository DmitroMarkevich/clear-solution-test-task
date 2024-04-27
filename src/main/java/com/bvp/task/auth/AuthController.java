package com.bvp.task.auth;

import com.bvp.task.errorhandling.ErrorResponse;
import com.bvp.task.security.jwt.JwtResponse;
import com.bvp.task.user.dto.LoginRequestDto;
import com.bvp.task.user.dto.UserRequestDto;
import com.bvp.task.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserResponseDto.class))
            }),
            @ApiResponse(responseCode = "401", description = "Validation failed",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            )
    })
    @Operation(summary = "Register User", description = "Creates a user account in the application")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDto registrationUser, BindingResult bindingResult) {
        return authService.registerUser(registrationUser, bindingResult);
    }

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JwtResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Validation failed",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            )
    })
    @Operation(summary = "Authenticate User", description = "Logs in the user and returns the authentication and refresh token")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDto authenticationRequest, BindingResult bindingResult) {
        return authService.authenticateUser(authenticationRequest, bindingResult);
    }
}

package com.bvp.task.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @Size(max = 50)
    @Email(message = "Invalid email format")
    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "^.{5,}@.*$", message = "Email must have at least 5 characters before @")
    @Schema(description = "The email address of the user", example = "user@example.com")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "The password of the user", example = "password123")
    private String password;
}
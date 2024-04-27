package com.bvp.task.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @Schema(description = "The unique identifier for the user")
    private UUID id;

    @NotBlank(message = "First name cannot be blank")
    @Schema(description = "The first name of the user", example = "Dmytro")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Schema(description = "The last name of the user", example = "Markevych")
    private String lastName;

    @NotNull(message = "Date of birth cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "The date of birth of the user", example = "2005-07-28")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number format")
    @Schema(description = "The phone number of the user", example = "+380986245727")
    private String phoneNumber;

    @Schema(description = "The address of the user", example = "123 Main Street, City")
    private String address;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Schema(description = "The email address of the user", example = "user@example.com")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^.{8,}$", message = "Password must be at least 8 characters long")
    @Schema(description = "The password of the user", example = "password123")
    private String password;
}

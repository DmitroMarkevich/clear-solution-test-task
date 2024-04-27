package com.bvp.task.user.dto;

import com.bvp.task.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    @Schema(description = "The unique identifier for the user", example = "2024-04-26T16:29:30.555+00:00")
    private UUID id;

    @Schema(description = "The timestamp when the user was created", example = "2024-04-26T16:29:30.555+00:00")
    private Timestamp createdAt;

    @Schema(description = "The timestamp when the user was last updated")
    private Timestamp updatedAt;

    @Schema(description = "The email address of the user", example = "user@example.com")
    private String email;

    @Schema(description = "The first name of the user", example = "Dmytro")
    private String firstName;

    @Schema(description = "The last name of the user", example = "Markevych")
    private String lastName;

    @Schema(description = "The date of birth of the user", example = "2005-07-28")
    private LocalDate dateOfBirth;

    @Schema(description = "The address of the user", example = "123 Main Street, City")
    private String address;

    @Schema(description = "The phone number of the user", example = "+380986245727")
    private String phoneNumber;

    @Schema(description = "The roles assigned to the user")
    private Set<Role> roles;

    @Schema(description = "Indicates if the user is soft deleted or not", example = "false")
    private boolean deleted;
}

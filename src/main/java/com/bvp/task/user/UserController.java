package com.bvp.task.user;

import com.bvp.task.user.dto.UserRequestDto;
import com.bvp.task.user.dto.UserUpdRequestDto;
import com.bvp.task.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }

    @PostMapping("/delete")
    @Operation(summary = "Delete user", description = "Delete a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> deleteUser(@RequestParam UUID id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by birth date range", description = "Search users by birth date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
    })
    public ResponseEntity<?> searchUsersByBirthDateRange(@RequestParam String fromDate, @RequestParam String toDate) {
        return userService.findUsersByBirthDateRange(LocalDate.parse(fromDate), LocalDate.parse(toDate));
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update user fields", description = "Update user fields by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User fields updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
    })
    public ResponseEntity<?> updateUserFields(@PathVariable UUID id,
                                              @Valid @RequestBody UserUpdRequestDto updateRequest,
                                              BindingResult bindingResult) {
        return userService.updateUserFields(id, updateRequest, bindingResult);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update user", description = "Update user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
    })
    public ResponseEntity<?> updateUser(@PathVariable UUID id,
                                        @Valid @RequestBody UserRequestDto userRequestDto,
                                        BindingResult bindingResult) {
        return userService.updateUser(id, userRequestDto, bindingResult);
    }
}

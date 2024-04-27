package com.bvp.task.user;

import com.bvp.task.errorhandling.ErrorUtils;
import com.bvp.task.errorhandling.exceptions.AccessDeniedException;
import com.bvp.task.errorhandling.exceptions.ItemNotCreatedException;
import com.bvp.task.errorhandling.exceptions.NoSuchElementException;
import com.bvp.task.user.dto.UserRequestDto;
import com.bvp.task.user.dto.UserResponseDto;
import com.bvp.task.user.dto.UserUpdRequestDto;
import com.bvp.task.user.entity.Role;
import com.bvp.task.user.entity.User;
import com.bvp.task.user.repository.RoleRepository;
import com.bvp.task.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${user.ageLimit}")
    private int ageLimit;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = findUserByEmail(email);

        if (user.isDeleted()) {
            throw new AccessDeniedException("Your account has been deleted.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
        );
    }

    public UserResponseDto register(UserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            throw new IllegalArgumentException("User can't be null.");
        }

        LocalDate localDateNow = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        LocalDate localDateBirth = userRequestDto.getDateOfBirth();
        int age = Period.between(localDateBirth, localDateNow).getYears();

        if (age < ageLimit) {
            throw new IllegalArgumentException("User must be at least " + ageLimit + " years old.");
        }

        Optional<User> existingUser = userRepository.findByEmail(userRequestDto.getEmail());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.isDeleted()) {
                return updateUserDetails(user, userRequestDto);
            } else {
                throw new IllegalArgumentException("User with this email already exists.");
            }
        }

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow();
        User user = userMapper.mapRequestDtoToEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(role));
        return userMapper.mapEntityToResponseDto(userRepository.save(user));
    }

    public ResponseEntity<?> deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (user.isDeleted()) {
            throw new AccessDeniedException("The user account has already been deleted.");
        }

        if (!isCurrentUserAdmin() || user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You don't have permission to delete this user.");
        }

        user.setDeleted(true);
        userRepository.save(user);

        return ResponseEntity.ok(String.format("User with id: '%s' was successfully deleted", userId));
    }

    public ResponseEntity<?> findUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("'From' date must be before 'To' date.");
        }

        if (isCurrentUserAdmin()) {
            Optional<List<User>> byDateOfBirthBetween = userRepository.findByDateOfBirthBetween(fromDate, toDate);

            if (byDateOfBirthBetween.isPresent()) {
                return ResponseEntity.ok(byDateOfBirthBetween.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found within the specified date range.");
            }
        }

        throw new AccessDeniedException("Insufficient privileges to view this information.");
    }

    public Page<User> getAllUsers(int page, int size) {
        if (isCurrentUserAdmin()) {
            Pageable pageable = PageRequest.of(page, size);
            return userRepository.findAll(pageable);
        } else {
            throw new AccessDeniedException("Insufficient privileges to view this information.");
        }
    }

    public ResponseEntity<?> updateUserFields(UUID userId, UserUpdRequestDto updateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotCreatedException(ErrorUtils.handleValidationErrors(bindingResult).toString());
        }

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }

        if (updateRequest == null) {
            throw new IllegalArgumentException("Update request cannot be null.");
        }

        if (!getCurrentUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to update this user.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getDateOfBirth() != null) {
            user.setDateOfBirth(updateRequest.getDateOfBirth());
        }
        if (updateRequest.getAddress() != null) {
            user.setAddress(updateRequest.getAddress());
        }
        if (updateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }

        userRepository.save(user);

        return ResponseEntity.ok("User fields updated successfully.");
    }

    public ResponseEntity<?> updateUser(UUID userId, UserRequestDto userRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotCreatedException(ErrorUtils.handleValidationErrors(bindingResult).toString());
        }

        if (!getCurrentUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to update this user.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setDateOfBirth(userRequestDto.getDateOfBirth());
        user.setAddress(userRequestDto.getAddress());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        userRepository.save(user);

        return ResponseEntity.ok("User updated successfully.");
    }

    private UserResponseDto updateUserDetails(User user, UserRequestDto userRequestDto) {
        if (!getCurrentUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this user.");
        }

        user.setDeleted(false);
        user.setLastName(userRequestDto.getLastName());
        user.setDateOfBirth(userRequestDto.getDateOfBirth());
        user.setAddress(userRequestDto.getAddress());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userRepository.save(user);
        return userMapper.mapEntityToResponseDto(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailAndDeletedFalse(email);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));
    }

    public UserResponseDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userMapper.mapEntityToResponseDto(findUserByEmail(email));
    }

    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}

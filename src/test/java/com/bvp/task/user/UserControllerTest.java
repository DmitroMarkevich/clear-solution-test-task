package com.bvp.task.user;

import com.bvp.task.user.dto.UserRequestDto;
import com.bvp.task.user.dto.UserUpdRequestDto;
import com.bvp.task.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetAllUsers() {
        Page<User> usersPage = mock(Page.class);
        when(userService.getAllUsers(0, 10)).thenReturn(usersPage);
        Page<User> result = userController.getAllUsers(0, 10);
        assertEquals(usersPage, result);
    }

    @Test
    public void testDeleteUser() {
        UUID userId = UUID.randomUUID();
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(userService.deleteUser(userId)).thenReturn(responseEntity);
        ResponseEntity<?> result = userController.deleteUser(userId);
        assertEquals(responseEntity, result);
    }

    @Test
    public void testSearchUsersByBirthDateRange() {
        String fromDate = "2024-01-01";
        String toDate = "2024-04-27";
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        when(userService
                .findUsersByBirthDateRange(LocalDate.parse(fromDate), LocalDate.parse(toDate)))
                .thenReturn(responseEntity);

        ResponseEntity<?> result = userController.searchUsersByBirthDateRange(fromDate, toDate);
        assertEquals(responseEntity, result);
    }

    @Test
    public void testUpdateUserFields() {
        UUID userId = UUID.randomUUID();
        UserUpdRequestDto updateRequest = new UserUpdRequestDto();
        BindingResult bindingResult = mock(BindingResult.class);
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(userService.updateUserFields(userId, updateRequest, bindingResult)).thenReturn(responseEntity);
        ResponseEntity<?> result = userController.updateUserFields(userId, updateRequest, bindingResult);
        assertEquals(responseEntity, result);
    }

    @Test
    public void testUpdateUser() {
        UUID userId = UUID.randomUUID();
        UserRequestDto userRequestDto = new UserRequestDto();
        BindingResult bindingResult = mock(BindingResult.class);
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(userService.updateUser(userId, userRequestDto, bindingResult)).thenReturn(responseEntity);
        ResponseEntity<?> result = userController.updateUser(userId, userRequestDto, bindingResult);
        assertEquals(responseEntity, result);
    }
}

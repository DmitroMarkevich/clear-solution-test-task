package com.bvp.task.user;

import com.bvp.task.user.dto.UserRequestDto;
import com.bvp.task.user.dto.UserResponseDto;
import com.bvp.task.user.entity.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserMapperTest {

    @Test
    public void testMapRequestDtoToEntity() {
        ModelMapper modelMapper = mock(ModelMapper.class);

        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setEmail("john.doe@example.com");

        User expectedUser = new User();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        expectedUser.setEmail("john.doe@example.com");

        when(modelMapper.map(requestDto, User.class)).thenReturn(expectedUser);
        UserMapper userMapper = new UserMapper(modelMapper);
        User result = userMapper.mapRequestDtoToEntity(requestDto);
        assertEquals(expectedUser, result);
    }

    @Test
    public void testMapEntityToResponseDto() {
        ModelMapper modelMapper = mock(ModelMapper.class);

        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        UserResponseDto expectedResponseDto = new UserResponseDto();
        expectedResponseDto.setId(id);
        expectedResponseDto.setFirstName("John");
        expectedResponseDto.setLastName("Doe");
        expectedResponseDto.setEmail("john.doe@example.com");

        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(expectedResponseDto);
        UserMapper userMapper = new UserMapper(modelMapper);
        UserResponseDto result = userMapper.mapEntityToResponseDto(user);
        assertEquals(expectedResponseDto, result);
    }
}

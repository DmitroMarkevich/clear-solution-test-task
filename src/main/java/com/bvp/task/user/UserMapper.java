package com.bvp.task.user;

import com.bvp.task.user.dto.UserRequestDto;
import com.bvp.task.user.dto.UserResponseDto;
import com.bvp.task.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public User mapRequestDtoToEntity(UserRequestDto userRequestDto) {
        return modelMapper.map(userRequestDto, User.class);
    }

    public UserResponseDto mapEntityToResponseDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}

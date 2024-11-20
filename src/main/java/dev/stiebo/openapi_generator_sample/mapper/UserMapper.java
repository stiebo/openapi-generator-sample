package dev.stiebo.openapi_generator_sample.mapper;

import dev.stiebo.openapi_generator_sample.domain.User;
import dev.stiebo.openapi_generator_sample.model.NewUserDto;
import dev.stiebo.openapi_generator_sample.model.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToUserDto(User user) {
        return new UserDto()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .status(user.getLocked() ? "LOCKED" : "UNLOCKED");
    }

    public User newUserDtoToUser (NewUserDto newUserDto) {
        return new User()
                .setName(newUserDto.getName())
                .setUsername(newUserDto.getUsername())
                .setPassword(newUserDto.getPassword());
    }

}

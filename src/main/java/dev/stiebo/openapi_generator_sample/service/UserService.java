package dev.stiebo.openapi_generator_sample.service;

import dev.stiebo.openapi_generator_sample.domain.User;
import dev.stiebo.openapi_generator_sample.model.ChangeAccessDto;
import dev.stiebo.openapi_generator_sample.model.ChangeRoleDto;
import dev.stiebo.openapi_generator_sample.model.NewUserDto;
import dev.stiebo.openapi_generator_sample.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserDto newUserDto);

    List<UserDto> listUsers();

    void deleteUser(String username);

    UserDto changeRole(ChangeRoleDto changeRoleDto);

    void changeAccess(ChangeAccessDto changeAccessDto);
}

package dev.stiebo.openapi_generator_sample.apiDelegateImpl;

import dev.stiebo.openapi_generator_sample.api.UserControllerApiDelegate;
import dev.stiebo.openapi_generator_sample.mapper.UserMapper;
import dev.stiebo.openapi_generator_sample.model.*;
import dev.stiebo.openapi_generator_sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserControllerApiDelegateImpl implements UserControllerApiDelegate {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserControllerApiDelegateImpl(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<UserDto> createUser(NewUserDto newUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(newUserDto));
    }

    @Override
    public ResponseEntity<ChangeAccess200Response> changeAccess(ChangeAccessDto changeAccessDto) {
        userService.changeAccess(changeAccessDto);
        return ResponseEntity.ok(new ChangeAccess200Response().status(
                String.format("User %s %s!", changeAccessDto.getUsername(),
                        changeAccessDto.getOperation().equals("LOCK") ?
                                "locked" : "unlocked")
        ));
    }

    @Override
    public ResponseEntity<UserDto> changeRole(ChangeRoleDto changeRoleDto) {
        return ResponseEntity.ok(userService.changeRole(changeRoleDto));
    }

    @Override
    public ResponseEntity<DeleteUserDto> deleteUser(String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok(new DeleteUserDto().username(username).status("Deleted successfully!"));
    }

    @Override
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }
}

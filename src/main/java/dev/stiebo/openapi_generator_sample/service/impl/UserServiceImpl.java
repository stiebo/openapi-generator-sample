package dev.stiebo.openapi_generator_sample.service.impl;

import dev.stiebo.openapi_generator_sample.domain.User;
import dev.stiebo.openapi_generator_sample.exception.*;
import dev.stiebo.openapi_generator_sample.mapper.UserMapper;
import dev.stiebo.openapi_generator_sample.model.ChangeAccessDto;
import dev.stiebo.openapi_generator_sample.model.ChangeRoleDto;
import dev.stiebo.openapi_generator_sample.model.NewUserDto;
import dev.stiebo.openapi_generator_sample.model.UserDto;
import dev.stiebo.openapi_generator_sample.repository.RoleRepository;
import dev.stiebo.openapi_generator_sample.repository.UserRepository;
import dev.stiebo.openapi_generator_sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder encoder,
                           UserMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @Override
    public UserDto createUser(NewUserDto newUserDto) {
        User user = mapper.newUserDtoToUser(newUserDto);
        user.setPassword(encoder.encode(user.getPassword()));
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
            throw new UserExistsException();
        }
        if (userRepository.count() == 0) {
            user.setRole(roleRepository.findByName("ADMINISTRATOR"));
            user.setLocked(false);
        } else {
            user.setRole(roleRepository.findByName("MERCHANT"));
            user.setLocked(true);
        }
        User savedUser = userRepository.save(user);
        return mapper.userToUserDto(savedUser);
    }

    @Override
    public List<UserDto> listUsers() {
        List<User> userList = userRepository.findAllByOrderByIdAsc();
        return userList.stream()
                .map(mapper::userToUserDto)
                .toList();
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    @Override
    public UserDto changeRole(ChangeRoleDto changeRoleDto) {
        User user = userRepository.findByUsernameIgnoreCase(changeRoleDto.getUsername())
                .orElseThrow(UserNotFoundException::new);
        if (user.getRole().getName().equals(changeRoleDto.getRole())) {
            throw new RoleAlreadyProvidedException();
        }
        user.setRole(roleRepository.findByName(changeRoleDto.getRole()));
        User savedUser = userRepository.save(user);
        return mapper.userToUserDto(savedUser);
    }

    @Override
    public void changeAccess(ChangeAccessDto changeAccessDto) {
        User user = userRepository.findByUsernameIgnoreCase(changeAccessDto.getUsername())
                .orElseThrow(UserNotFoundException::new);
        if (user.getRole().getName().equals("ADMINISTRATOR")) {
            throw new UnableToLockAdminException();
        }
        user.setLocked(changeAccessDto.getOperation().equals("LOCK"));
        userRepository.save(user);
    }
}

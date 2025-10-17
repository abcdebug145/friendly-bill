package com.project.friendly_bill.features.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.friendly_bill.features.user.dto.CreateUserDto;
import com.project.friendly_bill.features.user.dto.UpdateUserDto;
import com.project.friendly_bill.features.user.dto.UserDto;
import com.project.friendly_bill.features.user.entity.User;
import com.project.friendly_bill.features.user.mapper.UserMapper;
import com.project.friendly_bill.features.user.repository.UserRepository;
import com.project.friendly_bill.shared.exception.ErrorCode;
import com.project.friendly_bill.shared.exception.FBException;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserDto createUser(CreateUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new FBException(ErrorCode.EMAIL_ALREADY_IN_USE);
        }
        User user = userMapper.createUserFromDto(dto);
        String passwordHashed = passwordEncoder.encode(dto.getPassword());
        user.setPassword(passwordHashed);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto updateUser(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateEntityFromDto(dto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));

        if (user.isActive()) {
            user.setActive(false);
            userRepository.save(user);
        } else {
            userRepository.delete(user);
        }
    }
}

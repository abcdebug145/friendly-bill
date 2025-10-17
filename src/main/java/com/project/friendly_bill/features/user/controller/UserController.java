package com.project.friendly_bill.features.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.friendly_bill.features.user.dto.CreateUserDto;
import com.project.friendly_bill.features.user.dto.UpdateUserDto;
import com.project.friendly_bill.features.user.dto.UserDto;
import com.project.friendly_bill.features.user.service.UserService;
import com.project.friendly_bill.shared.common.api.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUserApi(@Valid @RequestBody CreateUserDto dto) {
        UserDto createdUser = userService.createUser(dto);
        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .success(true)
                .data(createdUser)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserByIdApi(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .success(true)
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmailApi(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .success(true)
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsersApi() {
        List<UserDto> users = userService.getAllUsers();
        ApiResponse<List<UserDto>> response = ApiResponse.<List<UserDto>>builder()
                .success(true)
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUserApi(@PathVariable Long id,@Valid @RequestBody UpdateUserDto dto) {
        UserDto updatedUser = userService.updateUser(id, dto);
        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .success(true)
                .data(updatedUser)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserApi(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }
}

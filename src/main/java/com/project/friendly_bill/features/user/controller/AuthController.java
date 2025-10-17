package com.project.friendly_bill.features.user.controller;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.friendly_bill.features.user.dto.AuthenticateDto;
import com.project.friendly_bill.features.user.dto.CreateUserDto;
import com.project.friendly_bill.features.user.dto.TokenDto;
import com.project.friendly_bill.features.user.service.AuthService;
import com.project.friendly_bill.shared.common.api.ApiResponse;
import com.project.friendly_bill.shared.utils.TokenUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    TokenUtils tokenUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginApi(@RequestBody AuthenticateDto authReq, HttpServletResponse response)
            throws Exception {
        var tokenPair = authService.authenticate(authReq);

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenPair.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (tokenUtils.REFRESH_EXPIRATION / 1000));
        response.addCookie(refreshTokenCookie);

        var apiResponse = ApiResponse.<TokenDto>builder()
                .data(TokenDto.builder().accessToken(tokenPair.getAccessToken()).build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerApi(@RequestBody CreateUserDto dto, HttpServletResponse response)
            throws Exception {
        var tokenPair = authService.register(dto);

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenPair.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (tokenUtils.REFRESH_EXPIRATION / 1000));
        response.addCookie(refreshTokenCookie);

        var apiResponse = ApiResponse.<TokenDto>builder()
                .data(TokenDto.builder().accessToken(tokenPair.getAccessToken()).build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logoutApi(HttpServletResponse response, HttpServletRequest request) throws ParseException {
        var accessToken = request.getHeader("Authorization").substring(7);
        var refreshToken = request.getCookies() != null ? 
                java.util.Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null) : null;

        authService.logout(accessToken, refreshToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        var apiResponse = ApiResponse.builder()
                .message("Logged out successfully")
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getCurrentUser() {
        var apiResponse = ApiResponse.builder()
                .message("Get current user successfully")
                .data(authService.getCurrentUser())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var refreshToken = request.getCookies() != null ? 
                java.util.Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null) : null;

        var tokenPair = authService.refreshToken(refreshToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenPair.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (tokenUtils.REFRESH_EXPIRATION / 1000));
        response.addCookie(refreshTokenCookie);

        var apiResponse = ApiResponse.<TokenDto>builder()
                .data(TokenDto.builder().accessToken(tokenPair.getAccessToken()).build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}

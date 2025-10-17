package com.project.friendly_bill.features.user.service;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.project.friendly_bill.features.user.dto.AuthenticateDto;
import com.project.friendly_bill.features.user.dto.CreateUserDto;
import com.project.friendly_bill.features.user.dto.TokenDto;
import com.project.friendly_bill.features.user.dto.UserDto;
import com.project.friendly_bill.features.user.entity.User;
import com.project.friendly_bill.features.user.mapper.UserMapper;
import com.project.friendly_bill.features.user.repository.UserRepository;
import com.project.friendly_bill.shared.exception.ErrorCode;
import com.project.friendly_bill.shared.exception.FBException;
import com.project.friendly_bill.shared.utils.CacheUtils;
import com.project.friendly_bill.shared.utils.TokenUtils;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    String NAMESPACE = "blacklist";

    TokenUtils tokenUtils;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserService userService;
    UserMapper userMapper;
    CacheUtils cacheUtils;

    public TokenDto authenticate(AuthenticateDto authReq) throws Exception {
        Optional<User> user = userRepository.findByUsernameOrEmail(authReq.getUsernameOrEmail(), authReq.getUsernameOrEmail());
        if (user.isEmpty() || !passwordEncoder.matches(authReq.getPassword(), user.get().getPassword())) {
            throw new FBException(ErrorCode.BAD_CREDENTIALS);
        }

        String accessToken = tokenUtils.generateAccessToken(user.get());
        String refreshToken = tokenUtils.generateRefreshToken(user.get());

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto refreshToken(String refreshToken) throws ParseException, Exception {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new FBException(ErrorCode.UNAUTHORIZED);
        }

        if (!tokenUtils.verifyToken(refreshToken)) 
            throw new FBException(ErrorCode.INVALID_TOKEN);

        JWTClaimsSet claims = tokenUtils.extractClaims(refreshToken).get();
        String refreshJti = claims.getJWTID();
        Long refreshTtl = claims.getExpirationTime().getTime() - System.currentTimeMillis();

        cacheUtils.putToCache(buildKey(NAMESPACE, refreshJti), refreshTtl);

        User user = userRepository.findByUsername(claims.getSubject()).get();

        String accessToken = tokenUtils.generateAccessToken(user);
        String refreshedToken = tokenUtils.generateRefreshToken(user);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshedToken)
                .build();
    }

    public TokenDto register(CreateUserDto dto) throws Exception {
        Optional<User> createdUser = userRepository.findByEmail(userService.createUser(dto).getEmail());
        String accessToken = tokenUtils.generateAccessToken(createdUser.get());
        String refreshToken = tokenUtils.generateRefreshToken(createdUser.get());

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserDto getCurrentUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user instanceof org.springframework.security.core.userdetails.User) {
            String email = ((org.springframework.security.core.userdetails.User) user).getUsername();
            Optional<User> foundUser = userRepository.findByEmail(email);
            if (foundUser.isEmpty()) {
                throw new FBException(ErrorCode.USER_NOT_FOUND);
            }
            return userMapper.toDto(foundUser.get());
        } else {
            throw new FBException(ErrorCode.UNAUTHORIZED);
        }
    }

    public void logout(String accessToken, String refreshToken) throws ParseException {
        SecurityContextHolder.clearContext();
        JWTClaimsSet claims = tokenUtils.extractClaims(refreshToken).get();
        String accessJti = claims.getStringClaim("access_jti");
        Long accessTtl = tokenUtils.extractClaims(accessToken).get().getExpirationTime().getTime() - System.currentTimeMillis();
        String refreshJti = claims.getJWTID();
        Long refreshTtl = claims.getExpirationTime().getTime() - System.currentTimeMillis();

        cacheUtils.putToCache(buildKey(NAMESPACE, accessJti), accessTtl);
        cacheUtils.putToCache(buildKey(NAMESPACE, refreshJti), refreshTtl);
    }

    private String buildKey(String namespace, String key) {
        return namespace + ":" + key;
    }
}

package com.project.friendly_bill.shared.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorCode {
    // General Errors
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),
    CONFLICT(409, "Conflict"),
    BAD_CREDENTIALS(401, "Bad credentials"),
    TOKEN_EXPIRED(401, "Token has expired"),
    INVALID_TOKEN(401, "Invalid token"),

    // User Errors
    USER_NOT_FOUND(1001, "User not found"),
    EMAIL_ALREADY_IN_USE(1002, "Email already in use"),
    INVALID_CREDENTIALS(1003, "Invalid credentials"),
    WEAK_PASSWORD(1004, "Password does not meet security requirements"),
    INVALID_PROVIDER(1005, "Invalid authentication provider"),
    OAUTH2_USER_INFO_NOT_FOUND(1006, "OAuth2 user info not found"),
    PROVIDER_ID_ALREADY_IN_USE(1007, "Provider ID already in use"),
    INVALID_QR_PAYMENT(1008, "Invalid QR payment format"),
    USERNAME_ALREADY_TAKEN(1009, "Username already taken")
    ;

    int code;
    String message;
}

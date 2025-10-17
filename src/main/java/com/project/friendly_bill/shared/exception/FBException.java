package com.project.friendly_bill.shared.exception;

public class FBException extends RuntimeException {
    private final ErrorCode errorCode;

    public FBException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

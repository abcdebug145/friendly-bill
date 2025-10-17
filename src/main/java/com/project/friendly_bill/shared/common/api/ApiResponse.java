package com.project.friendly_bill.shared.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude (JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    String message;
    T data;

    @Builder.Default
    boolean success = true;
    @Builder.Default    
    int code = 200;
}

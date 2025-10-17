package com.project.friendly_bill.features.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.friendly_bill.shared.common.enums.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    Long id;
    String username;
    String email;
    String avatar;
    String qrPayment;
    Role role;
}

package com.project.friendly_bill.features.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserDto {
    String username;
    String phone;
    String qrPayment;
    String avatar;
}

package com.project.friendly_bill.features.group.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateGroupMemberDto {
    @NotBlank
    String username; // username of user who wants to join (or can be inferred from auth)
}

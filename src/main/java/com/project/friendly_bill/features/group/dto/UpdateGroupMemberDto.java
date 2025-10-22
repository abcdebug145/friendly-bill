package com.project.friendly_bill.features.group.dto;

import com.project.friendly_bill.shared.common.enums.Position;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateGroupMemberDto {
    Position position;

    @Size(max = 50)
    String nickname;
}

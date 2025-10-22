package com.project.friendly_bill.features.group.dto;

import java.sql.Date;

import com.project.friendly_bill.shared.common.enums.Position;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GroupMemberDto {
    Long id;
    String username;
    Position position;
    String nickname;
    Date joinedAt;
    boolean isLeft;
}

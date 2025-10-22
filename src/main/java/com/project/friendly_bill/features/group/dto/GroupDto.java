package com.project.friendly_bill.features.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GroupDto {
    Long id;
    String name;
    String description;
    String imageUrl;
    String createdBy;
    int totalMembers;
}
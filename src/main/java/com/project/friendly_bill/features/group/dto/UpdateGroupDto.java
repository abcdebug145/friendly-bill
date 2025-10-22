package com.project.friendly_bill.features.group.dto;

import jakarta.validation.constraints.Size;
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
public class UpdateGroupDto {
    @Size(min = 3, max = 100, message = "Group name must be between 3 and 100 characters")
    String name;

    String imageUrl;
    
    @Size(max = 500, message = "Description must be less than 500 characters")
    String description;
}
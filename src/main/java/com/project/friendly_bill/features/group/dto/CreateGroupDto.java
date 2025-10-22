package com.project.friendly_bill.features.group.dto;

import jakarta.validation.constraints.NotBlank;
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
public class CreateGroupDto {
    @NotBlank(message = "Group name cannot be blank")
    @Size(min = 3, max = 100, message = "Group name must be between 3 and 100 characters")
    String name;
    
    @Size(max = 500, message = "Description must be less than 500 characters")
    String description;
}
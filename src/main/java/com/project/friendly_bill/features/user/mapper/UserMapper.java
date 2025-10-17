package com.project.friendly_bill.features.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.project.friendly_bill.features.user.dto.CreateUserDto;
import com.project.friendly_bill.features.user.dto.UpdateUserDto;
import com.project.friendly_bill.features.user.dto.UserDto;
import com.project.friendly_bill.features.user.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User createUserFromDto(CreateUserDto dto);
    UserDto toDto(User entity);
    void updateEntityFromDto(UpdateUserDto dto, @MappingTarget User entity);
}

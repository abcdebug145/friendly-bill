package com.project.friendly_bill.features.group.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.project.friendly_bill.features.group.dto.CreateGroupDto;
import com.project.friendly_bill.features.group.dto.GroupDto;
import com.project.friendly_bill.features.group.dto.UpdateGroupDto;
import com.project.friendly_bill.features.group.entity.Group;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {
    Group createGroupFromDto(CreateGroupDto dto);
    
    @Mapping(target = "createdBy", source = "createdBy.username")
    GroupDto toDto(Group entity);
    void updateEntityFromDto(UpdateGroupDto dto, @MappingTarget Group entity);
}
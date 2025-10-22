package com.project.friendly_bill.features.group.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.project.friendly_bill.features.group.dto.GroupMemberDto;
import com.project.friendly_bill.features.group.dto.UpdateGroupMemberDto;
import com.project.friendly_bill.features.group.entity.GroupMember;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMemberMapper {
    @Mapping(target = "username", source = "user.username")
    GroupMemberDto toDto(GroupMember entity);

    void updateEntityFromDto(UpdateGroupMemberDto dto, @MappingTarget GroupMember entity);
}

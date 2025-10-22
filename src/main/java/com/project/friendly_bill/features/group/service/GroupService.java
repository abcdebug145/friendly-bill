package com.project.friendly_bill.features.group.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.friendly_bill.features.group.dto.CreateGroupDto;
import com.project.friendly_bill.features.group.dto.GroupDto;
import com.project.friendly_bill.features.group.dto.UpdateGroupDto;
import com.project.friendly_bill.features.group.entity.Group;
import com.project.friendly_bill.features.group.entity.GroupMember;
import com.project.friendly_bill.features.group.mapper.GroupMapper;
import com.project.friendly_bill.features.group.repository.GroupMemberRepository;
import com.project.friendly_bill.features.group.repository.GroupRepository;
import com.project.friendly_bill.features.user.entity.User;
import com.project.friendly_bill.features.user.repository.UserRepository;
import com.project.friendly_bill.shared.common.enums.Position;
import com.project.friendly_bill.shared.exception.ErrorCode;
import com.project.friendly_bill.shared.exception.FBException;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GroupService {
    GroupRepository groupRepository;
    UserRepository userRepository;
    GroupMapper groupMapper;
    GroupMemberRepository groupMemberRepository;

    @Transactional
    public GroupDto createGroup(CreateGroupDto dto, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));
        
        Group group = groupMapper.createGroupFromDto(dto);
        group.setCreatedBy(currentUser);

        GroupMember creatorMember = GroupMember.builder()
                .group(group)
                .user(currentUser)
                .nickname(currentUser.getUsername())
                .position(Position.LEADER)
                .build();

        groupMemberRepository.save(creatorMember);
        
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toDto(savedGroup);
    }

    public GroupDto getGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));
        return groupMapper.toDto(group);
    }

    public List<GroupDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream()
                .map(groupMapper::toDto)
                .toList();
    }
    
    public List<GroupDto> getGroupsByUser(String username) {
        List<Group> groups = groupRepository.findMyGroups(username);
        return groups.stream()
                .map(groupMapper::toDto)
                .toList();
    }

    @Transactional
    public GroupDto updateGroup(Long id, UpdateGroupDto dto, String username) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));
        
        // Check if the user is the creator of the group
        if (!group.getCreatedBy().getUsername().equals(username)) {
            throw new FBException(ErrorCode.FORBIDDEN);
        }
        
        // Check if new name already exists (if name is being updated)
        if (dto.getName() != null && !dto.getName().equals(group.getName()) 
                && groupRepository.existsByName(dto.getName())) {
            throw new FBException(ErrorCode.GROUP_NAME_ALREADY_EXISTS);
        }
        
        groupMapper.updateEntityFromDto(dto, group);
        Group updatedGroup = groupRepository.save(group);
        return groupMapper.toDto(updatedGroup);
    }

    @Transactional
    public void deleteGroup(Long id, String username) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));
        
        // Check if the user is the creator of the group
        if (!group.getCreatedBy().getUsername().equals(username)) {
            throw new FBException(ErrorCode.FORBIDDEN);
        }
        
        groupRepository.delete(group);
    }
}
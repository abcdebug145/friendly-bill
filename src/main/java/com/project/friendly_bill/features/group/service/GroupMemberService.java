package com.project.friendly_bill.features.group.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.friendly_bill.features.group.dto.GroupMemberDto;
import com.project.friendly_bill.features.group.dto.UpdateGroupMemberDto;
import com.project.friendly_bill.features.group.entity.Group;
import com.project.friendly_bill.features.group.entity.GroupMember;
import com.project.friendly_bill.features.group.mapper.GroupMemberMapper;
import com.project.friendly_bill.features.group.repository.GroupMemberRepository;
import com.project.friendly_bill.features.group.repository.GroupRepository;
import com.project.friendly_bill.features.user.entity.User;
import com.project.friendly_bill.features.user.repository.UserRepository;
import com.project.friendly_bill.shared.exception.ErrorCode;
import com.project.friendly_bill.shared.exception.FBException;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GroupMemberService {
    GroupMemberRepository groupMemberRepository;
    GroupRepository groupRepository;
    UserRepository userRepository;
    GroupMemberMapper groupMemberMapper;

    @Transactional
    public GroupMemberDto requestToJoin(Long groupId, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));

        User user = userRepository.findByUsername(requesterUsername)
                    .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));

        // if already member
        groupMemberRepository.findByGroupAndUser(group, user).ifPresent(gm -> {
            throw new FBException(ErrorCode.CONFLICT);
        });

        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .nickname(requesterUsername)
                .position(com.project.friendly_bill.shared.common.enums.Position.MEMBER)
                .build();

        GroupMember saved = groupMemberRepository.save(member);
        return groupMemberMapper.toDto(saved);
    }

    public List<GroupMemberDto> listMembers(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));

        List<GroupMember> members = groupMemberRepository.findByGroup(group);
        return members.stream().map(groupMemberMapper::toDto).toList();
    }

    public GroupMemberDto getMember(Long groupId, Long memberId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));

        GroupMember gm = groupMemberRepository.findById(memberId)
                .orElseThrow(() -> new FBException(ErrorCode.NOT_FOUND));
        if (!gm.getGroup().getId().equals(group.getId())) {
            throw new FBException(ErrorCode.NOT_FOUND);
        }
        return groupMemberMapper.toDto(gm);
    }

    @Transactional
    public GroupMemberDto approveMember(Long groupId, Long memberId, String approverUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));

        // verify approver is leader
        User approver = userRepository.findByUsername(approverUsername)
                .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));

        if (!group.getCreatedBy().getId().equals(approver.getId())) {
            throw new FBException(ErrorCode.FORBIDDEN);
        }

        GroupMember gm = groupMemberRepository.findById(memberId)
                .orElseThrow(() -> new FBException(ErrorCode.NOT_FOUND));

        if (!gm.getGroup().getId().equals(group.getId())) {
            throw new FBException(ErrorCode.NOT_FOUND);
        }

        // approve logic can be extended (e.g., set isLeft=false)
    gm.setLeft(false);
        GroupMember saved = groupMemberRepository.save(gm);
        return groupMemberMapper.toDto(saved);
    }

    @Transactional
    public void removeMember(Long groupId, Long memberId, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));

        if (!group.getCreatedBy().getId().equals(requester.getId())) {
            throw new FBException(ErrorCode.FORBIDDEN);
        }

        GroupMember gm = groupMemberRepository.findById(memberId)
                .orElseThrow(() -> new FBException(ErrorCode.NOT_FOUND));

        if (!gm.getGroup().getId().equals(group.getId())) {
            throw new FBException(ErrorCode.NOT_FOUND);
        }

        groupMemberRepository.delete(gm);
    }

    @Transactional
    public GroupMemberDto updateMember(Long groupId, Long memberId, UpdateGroupMemberDto dto, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));

        // only group leader can change position
        GroupMember gm = groupMemberRepository.findById(memberId)
                .orElseThrow(() -> new FBException(ErrorCode.NOT_FOUND));
        if (!gm.getGroup().getId().equals(group.getId())) {
            throw new FBException(ErrorCode.NOT_FOUND);
        }

        if (dto.getPosition() != null && !group.getCreatedBy().getId().equals(requester.getId())) {
            throw new FBException(ErrorCode.FORBIDDEN);
        }

        groupMemberMapper.updateEntityFromDto(dto, gm);
        GroupMember saved = groupMemberRepository.save(gm);
        return groupMemberMapper.toDto(saved);
    }

    @Transactional
    public void leaveGroup(Long groupId, String username) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new FBException(ErrorCode.GROUP_NOT_FOUND));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new FBException(ErrorCode.USER_NOT_FOUND));

        GroupMember gm = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new FBException(ErrorCode.NOT_FOUND));

        // leader cannot leave (or transfer leader) - business rule
        if (group.getCreatedBy().getId().equals(user.getId())) {
            throw new FBException(ErrorCode.FORBIDDEN);
        }

        groupMemberRepository.delete(gm);
    }
}

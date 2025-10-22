package com.project.friendly_bill.features.group.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.friendly_bill.features.group.dto.GroupMemberDto;
import com.project.friendly_bill.features.group.dto.UpdateGroupMemberDto;
import com.project.friendly_bill.features.group.service.GroupMemberService;
import com.project.friendly_bill.shared.common.api.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/groups/{groupId}/members")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GroupMemberController {
    GroupMemberService groupMemberService;

    @PostMapping
    public ResponseEntity<ApiResponse<GroupMemberDto>> requestToJoin(@PathVariable Long groupId,
            @AuthenticationPrincipal(expression = "claims['sub']") String username) {
        GroupMemberDto created = groupMemberService.requestToJoin(groupId, username);
        ApiResponse<GroupMemberDto> response = ApiResponse.<GroupMemberDto>builder()
                .success(true)
                .data(created)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupMemberDto>>> listMembers(@PathVariable Long groupId) {
        List<GroupMemberDto> members = groupMemberService.listMembers(groupId);
        ApiResponse<List<GroupMemberDto>> response = ApiResponse.<List<GroupMemberDto>>builder()
                .success(true)
                .data(members)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<GroupMemberDto>> getMember(@PathVariable Long groupId, @PathVariable Long memberId) {
        GroupMemberDto member = groupMemberService.getMember(groupId, memberId);
        ApiResponse<GroupMemberDto> response = ApiResponse.<GroupMemberDto>builder()
                .success(true)
                .data(member)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<ApiResponse<GroupMemberDto>> updateMember(@PathVariable Long groupId, @PathVariable Long memberId,
            @Valid @RequestBody UpdateGroupMemberDto dto,
            @AuthenticationPrincipal(expression = "claims['sub']") String username) {
        GroupMemberDto updated = groupMemberService.updateMember(groupId, memberId, dto, username);
        ApiResponse<GroupMemberDto> response = ApiResponse.<GroupMemberDto>builder()
                .success(true)
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(@PathVariable Long groupId, @PathVariable Long memberId,
            @AuthenticationPrincipal(expression = "claims['sub']") String username) {
        groupMemberService.removeMember(groupId, memberId, username);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> leaveGroup(@PathVariable Long groupId,
            @AuthenticationPrincipal(expression = "claims['sub']") String username) {
        groupMemberService.leaveGroup(groupId, username);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }
}

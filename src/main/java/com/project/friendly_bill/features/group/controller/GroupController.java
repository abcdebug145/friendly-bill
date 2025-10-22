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

import com.project.friendly_bill.features.group.dto.CreateGroupDto;
import com.project.friendly_bill.features.group.dto.GroupDto;
import com.project.friendly_bill.features.group.dto.UpdateGroupDto;
import com.project.friendly_bill.features.group.service.GroupService;
import com.project.friendly_bill.shared.common.api.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GroupController {
	GroupService groupService;

	@PostMapping
	public ResponseEntity<ApiResponse<GroupDto>> createGroupApi(@Valid @RequestBody CreateGroupDto dto,
			@AuthenticationPrincipal(expression = "claims['sub']") String username) {
		GroupDto createdGroup = groupService.createGroup(dto, username);
		ApiResponse<GroupDto> response = ApiResponse.<GroupDto>builder()
				.success(true)
				.data(createdGroup)
				.build();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<GroupDto>> getGroupByIdApi(@PathVariable Long id) {
		GroupDto group = groupService.getGroupById(id);
		ApiResponse<GroupDto> response = ApiResponse.<GroupDto>builder()
				.success(true)
				.data(group)
				.build();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/my-groups")
	public ResponseEntity<ApiResponse<List<GroupDto>>> getUserGroupsApi(@AuthenticationPrincipal(expression = "claims['sub']") String username) {
		List<GroupDto> groups = groupService.getGroupsByUser(username);
		ApiResponse<List<GroupDto>> response = ApiResponse.<List<GroupDto>>builder()
				.success(true)
				.data(groups)
				.build();
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<GroupDto>>> getAllGroupsApi() {
		List<GroupDto> groups = groupService.getAllGroups();
		ApiResponse<List<GroupDto>> response = ApiResponse.<List<GroupDto>>builder()
				.success(true)
				.data(groups)
				.build();
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<GroupDto>> updateGroupApi(@PathVariable Long id,
			@Valid @RequestBody UpdateGroupDto dto,
			@AuthenticationPrincipal(expression = "claims['sub']") String username) {
		GroupDto updatedGroup = groupService.updateGroup(id, dto, username);
		ApiResponse<GroupDto> response = ApiResponse.<GroupDto>builder()
				.success(true)
				.data(updatedGroup)
				.build();
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteGroupApi(@PathVariable Long id,
			@AuthenticationPrincipal(expression = "claims['sub']") String username) {
		groupService.deleteGroup(id, username);
		ApiResponse<Void> response = ApiResponse.<Void>builder()
				.success(true)
				.build();
		return ResponseEntity.ok(response);
	}
}
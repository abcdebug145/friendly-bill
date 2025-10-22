package com.project.friendly_bill.features.group.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.friendly_bill.features.group.entity.Group;
import com.project.friendly_bill.features.group.entity.GroupMember;
import com.project.friendly_bill.features.user.entity.User;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
	Optional<GroupMember> findByGroupAndUser(Group group, User user);
	List<GroupMember> findByGroup(Group group);
	List<GroupMember> findByUser(User user);
}

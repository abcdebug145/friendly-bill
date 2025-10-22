package com.project.friendly_bill.features.group.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.friendly_bill.features.group.entity.Group;
import com.project.friendly_bill.features.user.entity.User;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByName(String name);
    List<Group> findByCreatedBy(User user);
    Optional<Group> findByNameAndCreatedBy(String name, User createdBy);

    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.user.username = :username")
    List<Group> findMyGroups(String username);
}
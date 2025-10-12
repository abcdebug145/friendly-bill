package com.project.friendly_bill.features.group.entity;

import java.sql.Date;

import com.project.friendly_bill.features.user.entity.User;
import com.project.friendly_bill.shared.common.enums.Position;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter @Setter
@Builder
@Table(name = "group_members")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GroupMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    User addedBy;

    @Builder.Default
    Position position = Position.MEMBER;

    @Column(length = 50)
    String nickname;

    @Builder.Default
    Date joinedAt = new Date(System.currentTimeMillis());

    @Builder.Default
    boolean isLeft = false;
}

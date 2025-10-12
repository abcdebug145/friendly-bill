package com.project.friendly_bill.features.user.entity;

import java.sql.Date;
import java.util.List;

import com.project.friendly_bill.features.expense.entity.Expense;
import com.project.friendly_bill.features.expense.entity.ExpenseSplit;
import com.project.friendly_bill.features.expense.entity.Settlement;
import com.project.friendly_bill.features.group.entity.Group;
import com.project.friendly_bill.features.group.entity.GroupMember;
import com.project.friendly_bill.features.group.entity.Message;
import com.project.friendly_bill.shared.common.enums.Provider;
import com.project.friendly_bill.shared.common.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Getter @Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 50)
    String username;

    @Column(nullable = true, length = 100, unique = true)
    String email;

    @Column(nullable = true, length = 15)
    String phone;

    String avatar;
    String qrPayment;

    @Builder.Default
    Role role = Role.USER;

    @Column(nullable = false)
    String password;

    @Builder.Default
    Provider provider = Provider.LOCAL;

    @Column(unique = true)
    String providerId;

    boolean isActive;

    @Builder.Default
    Date createdAt = new Date(System.currentTimeMillis());

    // for relationship mapping
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Group> groups;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<GroupMember> groupMembers;

    @OneToMany(mappedBy = "addedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    List<GroupMember> addedMembers;

    @OneToMany(mappedBy = "payer", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Expense> expenses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ExpenseSplit> expenseSplits;

    @OneToMany(mappedBy = "payee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Settlement> payeeSettlements;

    @OneToMany(mappedBy = "payer", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Settlement> payerSettlements;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Message> sentMessages;
}

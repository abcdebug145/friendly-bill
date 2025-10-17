package com.project.friendly_bill.features.group.entity;

import java.util.List;

import com.project.friendly_bill.features.expense.entity.Expense;
import com.project.friendly_bill.features.expense.entity.Settlement;
import com.project.friendly_bill.features.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "`groups`")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String description;

    @ManyToOne
    @JoinColumn(name = "created_by")
    User createdBy;

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.ALL)
    List<GroupMember> groupMembers;

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Expense> expenses;

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Message> messages;

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Settlement> settlements;
}

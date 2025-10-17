package com.project.friendly_bill.features.expense.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Currency;
import java.util.List;

import com.project.friendly_bill.features.group.entity.Group;
import com.project.friendly_bill.features.user.entity.User;
import com.project.friendly_bill.shared.common.enums.ExpenseCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "expenses")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String description;
    BigDecimal amount;

    @Builder.Default
    Currency currency = Currency.getInstance("VND");

    ExpenseCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id")
    User payer;

    @Builder.Default
    Date expenseDate = new Date(System.currentTimeMillis());

    @Builder.Default
    Date paidAt = new Date(System.currentTimeMillis());

    @OneToMany(mappedBy = "expense", orphanRemoval = true, cascade = CascadeType.ALL)
    List<ExpenseSplit> splits;
}

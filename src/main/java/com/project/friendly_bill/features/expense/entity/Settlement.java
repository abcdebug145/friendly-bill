package com.project.friendly_bill.features.expense.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Currency;

import com.project.friendly_bill.features.group.entity.Group;
import com.project.friendly_bill.features.user.entity.User;

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
@Table(name = "settlements")
@Getter @Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id")
    User payer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payee_id")
    User payee;

    BigDecimal amount;

    @Builder.Default
    Currency currency = Currency.getInstance("VND");

    boolean isSettled;

    @Builder.Default
    Date settledAt = null;

    @Builder.Default
    Date createdAt = new Date(System.currentTimeMillis());

    String notes;
}

package com.project.friendly_bill.shared.common.enums;

public enum ExpenseCategory {
    TRANSPORTATION("Transportation", "Transportation expenses"),
    ACCOMMODATION("Accommodation", "Accommodation expenses"),
    FOOD("Food", "Food"),
    DRINKS("Drinks", "Drinks / parties"),
    TICKETS("Tickets", "Tickets for sightseeing / events"),
    SHOPPING("Shopping", "Shopping / souvenirs"),
    ENTERTAINMENT("Entertainment", "Entertainment / recreation"),
    FUEL("Fuel", "Fuel / vehicle rental"),
    OTHER("Other", "Other");

    private final String displayName;
    private final String description;

    ExpenseCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}

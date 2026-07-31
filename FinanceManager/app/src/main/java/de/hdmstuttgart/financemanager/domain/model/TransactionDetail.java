package com.hdmstuttgart.financemanager.domain.model;

/**
 * Domain model representing a financial transaction.
 * Decoupled from data layer (Room) implementation.
 */
public class TransactionDetail {
    private final int id;
    private final int imageResource;
    private final String purpose;
    private final String amount; // Format: "+10,00€" or "-10,00€"
    private final String date;
    private final String category;
    private final String method;

    public TransactionDetail(int id, int imageResource, String purpose, String amount, String date, String category, String method) {
        this.id = id;
        this.imageResource = imageResource;
        this.purpose = purpose;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.method = method;
    }

    public int getId() { return id; }
    public int getImageResource() { return imageResource; }
    public String getPurpose() { return purpose; }
    public String getAmount() { return amount; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public String getMethod() { return method; }
}

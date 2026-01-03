package model;

import java.time.LocalDate;
import java.util.UUID;

/** Expense with details including date, amount, merchant, etc. */
public class Expense {
    private UUID id;
    private LocalDate date;
    private double amount;
    private String merchant;
    private String item;
    private Category category;

    /** Expense with specified details. */
    public Expense() {
    }

    /** Creates expense with specific details.
     * Generates unique ID for the expense.
     *
     * @param date the date of the expense
     * @param amount the amount spent
     * @param merchant the merchant where the expense occurred
     * @param item the item purchased
     * @param category the category of the expense
     * */
    public Expense(LocalDate date, double amount, String merchant, String item, Category category) {
        this.id = UUID.randomUUID();
        this.date = date;
        this.amount = amount;
        this.merchant = merchant;
        this.item = item;
        this.category = category;
    }

    /** Returns the id.
     * @return id*/
    public UUID getId() {
        return this.id;
    }

    /** Returns the date.
     * @return date*/
    public LocalDate getDate() {
        return this.date;
    }

    /** Returns the amount.
     * @return amount*/
    public double getAmount() {
        return this.amount;
    }

    /** Returns the merchant.
     * @return merchant*/
    public String getMerchant() {
        return this.merchant;
    }

    /** Returns the item.
     * @return item*/
    public String getItem() {
        return this.item;
    }

    /** Returns the category.
     * @return category*/
    public Category getCategory() {
        return this.category;
    }

    /** Sets the date.
     * @param date date*/
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /** Sets the amount.
     * @param amount amount*/
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /** Sets the merchant.
     * @param merchant merchant*/
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    /** Sets the item.
     * @param item item*/
    public void setItem(String item) {
        this.item = item;
    }

    /** Sets the category.
     * @param category category*/
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("%s, %.2f, %s, %s",
                this.date, this.amount, this.category, this.item);
    }
}
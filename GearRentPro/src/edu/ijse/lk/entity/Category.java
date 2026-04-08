package edu.ijse.lk.entity;

import java.math.BigDecimal;

public class Category {
    private int categoryId;
    private String name;
    private String description;
    private BigDecimal priceFactor;
    private BigDecimal weekendMultiplier;
    private BigDecimal lateFeePerDay;
    private boolean active;

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPriceFactor() { return priceFactor; }
    public void setPriceFactor(BigDecimal priceFactor) { this.priceFactor = priceFactor; }
    public BigDecimal getWeekendMultiplier() { return weekendMultiplier; }
    public void setWeekendMultiplier(BigDecimal weekendMultiplier) { this.weekendMultiplier = weekendMultiplier; }
    public BigDecimal getLateFeePerDay() { return lateFeePerDay; }
    public void setLateFeePerDay(BigDecimal lateFeePerDay) { this.lateFeePerDay = lateFeePerDay; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() { return categoryId + " - " + name; }
}

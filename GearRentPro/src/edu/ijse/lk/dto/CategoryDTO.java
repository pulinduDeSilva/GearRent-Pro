package edu.ijse.lk.dto;

import java.math.BigDecimal;

public class CategoryDTO {
    public int categoryId;
    public String name;
    public String description;
    public BigDecimal priceFactor;
    public BigDecimal weekendMultiplier;
    public BigDecimal lateFeePerDay;
    public boolean active;
}

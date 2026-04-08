package edu.ijse.lk.entity;

import java.math.BigDecimal;

public class Equipment {
    private int equipmentId;
    private int categoryId;
    private int branchId;
    private String brand;
    private String model;
    private int purchaseYear;
    private BigDecimal baseDailyPrice;
    private BigDecimal securityDeposit;
    private EquipmentStatus status;

    public int getEquipmentId() { return equipmentId; }
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getPurchaseYear() { return purchaseYear; }
    public void setPurchaseYear(int purchaseYear) { this.purchaseYear = purchaseYear; }
    public BigDecimal getBaseDailyPrice() { return baseDailyPrice; }
    public void setBaseDailyPrice(BigDecimal baseDailyPrice) { this.baseDailyPrice = baseDailyPrice; }
    public BigDecimal getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(BigDecimal securityDeposit) { this.securityDeposit = securityDeposit; }
    public EquipmentStatus getStatus() { return status; }
    public void setStatus(EquipmentStatus status) { this.status = status; }

    @Override
    public String toString() { return equipmentId + " - " + brand + " " + model; }
}

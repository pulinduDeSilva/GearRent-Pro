package edu.ijse.lk.entity;

import java.math.BigDecimal;

public class Membership {
    private int membershipId;
    private String levelName;
    private BigDecimal discountPercentage;

    public int getMembershipId() { return membershipId; }
    public void setMembershipId(int membershipId) { this.membershipId = membershipId; }
    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }

    @Override
    public String toString() { return membershipId + " - " + levelName; }
}

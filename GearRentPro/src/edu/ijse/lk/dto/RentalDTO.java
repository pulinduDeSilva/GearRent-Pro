package edu.ijse.lk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalDTO {
    public int rentalId;
    public int equipmentId;
    public int customerId;
    public int branchId;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalDate actualReturnDate;
    public BigDecimal rentalAmount;
    public BigDecimal securityDeposit;
    public BigDecimal membershipDiscount;
    public BigDecimal longRentalDiscount;
    public BigDecimal lateFee;
    public BigDecimal damageCharge;
    public BigDecimal finalAmount;
    public BigDecimal settlementAmount;
    public String paymentStatus;
    public String rentalStatus;
}

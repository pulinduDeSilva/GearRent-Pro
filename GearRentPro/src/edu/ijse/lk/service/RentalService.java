package edu.ijse.lk.service;

import edu.ijse.lk.dao.CategoryDAO;
import edu.ijse.lk.dao.CustomerDAO;
import edu.ijse.lk.dao.EquipmentDAO;
import edu.ijse.lk.dao.MembershipDAO;
import edu.ijse.lk.dao.RentalDAO;
import edu.ijse.lk.dao.impl.CategoryDAOImpl;
import edu.ijse.lk.dao.impl.CustomerDAOImpl;
import edu.ijse.lk.dao.impl.EquipmentDAOImpl;
import edu.ijse.lk.dao.impl.MembershipDAOImpl;
import edu.ijse.lk.dao.impl.RentalDAOImpl;
import edu.ijse.lk.dto.RentalDTO;
import edu.ijse.lk.entity.*;
import edu.ijse.lk.util.DBConnection;
import edu.ijse.lk.util.ValidationUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    private final RentalDAO rentalDAO = new RentalDAOImpl();
    private final EquipmentDAO equipmentDAO = new EquipmentDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private final MembershipDAO membershipDAO = new MembershipDAOImpl();

    public int create(RentalDTO dto) {
        ValidationUtil.validateDateRange(dto.startDate, dto.endDate);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            try {
                int id = createInternal(con, dto);
                con.commit();
                return id;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create rental: " + ex.getMessage(), ex);
        }
    }

    int createInternal(Connection con, RentalDTO dto) throws Exception {
        Equipment equipment = equipmentDAO.findById(con, dto.equipmentId);
        ValidationUtil.require(equipment != null, "Equipment not found.");
        ValidationUtil.require(equipment.getStatus() == EquipmentStatus.Available || equipment.getStatus() == EquipmentStatus.Reserved, "Equipment is not available for rental.");
        ValidationUtil.require(!equipmentDAO.hasRentalOrReservationConflict(con, dto.equipmentId, dto.startDate, dto.endDate), "Equipment already reserved/rented in the selected period.");

        Category category = categoryDAO.findById(con, equipment.getCategoryId());
        ValidationUtil.require(category != null && category.isActive(), "Category is not active.");

        Customer customer = customerDAO.findById(con, dto.customerId);
        ValidationUtil.require(customer != null, "Customer not found.");

        BigDecimal rentalAmount = calculateRentalAmount(equipment.getBaseDailyPrice(), category.getPriceFactor(), category.getWeekendMultiplier(), dto.startDate, dto.endDate);
        long rentalDays = dto.endDate.toEpochDay() - dto.startDate.toEpochDay() + 1;

        BigDecimal longDiscount = rentalDays > 7 ? rentalAmount.multiply(new BigDecimal("0.05")) : BigDecimal.ZERO;
        BigDecimal membershipDiscount = BigDecimal.ZERO;
        if (customer.getMembershipId() != null) {
            Membership membership = membershipDAO.findById(con, customer.getMembershipId());
            if (membership != null) {
                membershipDiscount = rentalAmount.multiply(membership.getDiscountPercentage()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            }
        }

        BigDecimal finalAmount = rentalAmount.subtract(longDiscount).subtract(membershipDiscount).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);

        Rental rental = new Rental();
        rental.setEquipmentId(dto.equipmentId);
        rental.setCustomerId(dto.customerId);
        rental.setBranchId(dto.branchId);
        rental.setStartDate(dto.startDate);
        rental.setEndDate(dto.endDate);
        rental.setRentalAmount(rentalAmount.setScale(2, RoundingMode.HALF_UP));
        rental.setSecurityDeposit(equipment.getSecurityDeposit());
        rental.setLongRentalDiscount(longDiscount.setScale(2, RoundingMode.HALF_UP));
        rental.setMembershipDiscount(membershipDiscount.setScale(2, RoundingMode.HALF_UP));
        rental.setLateFee(BigDecimal.ZERO);
        rental.setDamageCharge(BigDecimal.ZERO);
        rental.setFinalAmount(finalAmount);
        rental.setSettlementAmount(BigDecimal.ZERO);
        rental.setPaymentStatus(PaymentStatus.Unpaid);
        rental.setRentalStatus(RentalStatus.Active);

        int id = rentalDAO.save(con, rental);
        equipmentDAO.updateStatus(con, dto.equipmentId, EquipmentStatus.Rented);
        return id;
    }

    public void returnRental(int rentalId, LocalDate actualReturnDate, BigDecimal damageCharge) {
        ValidationUtil.require(actualReturnDate != null, "Actual return date is required.");
        ValidationUtil.require(damageCharge != null && damageCharge.compareTo(BigDecimal.ZERO) >= 0, "Damage charge must be >= 0.");

        try {
            Connection con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            try {
                Rental rental = rentalDAO.findById(con, rentalId);
                ValidationUtil.require(rental != null, "Rental not found.");
                ValidationUtil.require(rental.getRentalStatus() == RentalStatus.Active || rental.getRentalStatus() == RentalStatus.Overdue, "Rental is not active.");

                Equipment equipment = equipmentDAO.findById(con, rental.getEquipmentId());
                Category category = categoryDAO.findById(con, equipment.getCategoryId());

                long daysLate = Math.max(0, actualReturnDate.toEpochDay() - rental.getEndDate().toEpochDay());
                BigDecimal lateFee = category.getLateFeePerDay().multiply(BigDecimal.valueOf(daysLate));
                BigDecimal penalties = lateFee.add(damageCharge);
                BigDecimal settlement = penalties.subtract(rental.getSecurityDeposit());

                rental.setActualReturnDate(actualReturnDate);
                rental.setLateFee(lateFee);
                rental.setDamageCharge(damageCharge);
                rental.setSettlementAmount(settlement.setScale(2, RoundingMode.HALF_UP));
                rental.setRentalStatus(RentalStatus.Returned);
                rental.setPaymentStatus(settlement.compareTo(BigDecimal.ZERO) > 0 ? PaymentStatus.Partially_Paid : PaymentStatus.Paid);

                rentalDAO.updateReturnDetails(con, rental);
                equipmentDAO.updateStatus(con, rental.getEquipmentId(), EquipmentStatus.Available);
                con.commit();
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to return rental: " + ex.getMessage(), ex);
        }
    }

    public List<RentalDTO> list(Integer branchId) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            List<RentalDTO> out = new ArrayList<>();
            for (Rental r : rentalDAO.findAll(con, branchId)) out.add(toDTO(r));
            return out;
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public List<RentalDTO> overdue(Integer branchId) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            List<Rental> rentals = rentalDAO.findOverdue(con, branchId, LocalDate.now());
            for (Rental r : rentals) {
                if (r.getRentalStatus() == RentalStatus.Active) rentalDAO.updateStatus(con, r.getRentalId(), RentalStatus.Overdue);
            }
            List<RentalDTO> out = new ArrayList<>();
            for (Rental r : rentals) out.add(toDTO(r));
            return out;
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    private BigDecimal calculateRentalAmount(BigDecimal baseDailyPrice, BigDecimal factor, BigDecimal weekendMultiplier, LocalDate start, LocalDate end) {
        BigDecimal total = BigDecimal.ZERO;
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            BigDecimal day = baseDailyPrice.multiply(factor);
            if (d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY) {
                day = day.multiply(weekendMultiplier);
            }
            total = total.add(day);
        }
        return total;
    }

    private RentalDTO toDTO(Rental r) {
        RentalDTO dto = new RentalDTO();
        dto.rentalId = r.getRentalId();
        dto.equipmentId = r.getEquipmentId();
        dto.customerId = r.getCustomerId();
        dto.branchId = r.getBranchId();
        dto.startDate = r.getStartDate();
        dto.endDate = r.getEndDate();
        dto.actualReturnDate = r.getActualReturnDate();
        dto.rentalAmount = r.getRentalAmount();
        dto.securityDeposit = r.getSecurityDeposit();
        dto.membershipDiscount = r.getMembershipDiscount();
        dto.longRentalDiscount = r.getLongRentalDiscount();
        dto.lateFee = r.getLateFee();
        dto.damageCharge = r.getDamageCharge();
        dto.finalAmount = r.getFinalAmount();
        dto.settlementAmount = r.getSettlementAmount();
        dto.paymentStatus = r.getPaymentStatus().toDbValue();
        dto.rentalStatus = r.getRentalStatus().name();
        return dto;
    }
}

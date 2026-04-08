package edu.ijse.lk.dao.impl;

import edu.ijse.lk.dao.RentalDAO;
import edu.ijse.lk.dto.RevenueReportDTO;
import edu.ijse.lk.dto.UtilizationReportDTO;
import edu.ijse.lk.entity.PaymentStatus;
import edu.ijse.lk.entity.Rental;
import edu.ijse.lk.entity.RentalStatus;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalDAOImpl implements RentalDAO {
    @Override
    public int save(Connection connection, Rental rental) throws SQLException {
        String sql = "INSERT INTO rental(equipment_id,customer_id,branch_id,start_date,end_date,rental_amount,security_deposit,membership_discount,long_rental_discount,late_fee,damage_charge,final_amount,settlement_amount,payment_status,rental_status) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, rental.getEquipmentId());
            ps.setInt(2, rental.getCustomerId());
            ps.setInt(3, rental.getBranchId());
            ps.setDate(4, Date.valueOf(rental.getStartDate()));
            ps.setDate(5, Date.valueOf(rental.getEndDate()));
            ps.setBigDecimal(6, rental.getRentalAmount());
            ps.setBigDecimal(7, rental.getSecurityDeposit());
            ps.setBigDecimal(8, rental.getMembershipDiscount());
            ps.setBigDecimal(9, rental.getLongRentalDiscount());
            ps.setBigDecimal(10, rental.getLateFee());
            ps.setBigDecimal(11, rental.getDamageCharge());
            ps.setBigDecimal(12, rental.getFinalAmount());
            ps.setBigDecimal(13, rental.getSettlementAmount());
            ps.setString(14, rental.getPaymentStatus().toDbValue());
            ps.setString(15, rental.getRentalStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    @Override
    public Rental findById(Connection connection, int rentalId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM rental WHERE rental_id=?")) {
            ps.setInt(1, rentalId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    @Override
    public List<Rental> findAll(Connection connection, Integer branchId) throws SQLException {
        String sql = branchId == null ? "SELECT * FROM rental ORDER BY rental_id DESC" : "SELECT * FROM rental WHERE branch_id=? ORDER BY rental_id DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (branchId != null) ps.setInt(1, branchId);
            List<Rental> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
            return list;
        }
    }

    @Override
    public List<Rental> findOverdue(Connection connection, Integer branchId, LocalDate today) throws SQLException {
        String sql = branchId == null
                ? "SELECT * FROM rental WHERE rental_status IN ('Active','Overdue') AND end_date < ? ORDER BY end_date"
                : "SELECT * FROM rental WHERE branch_id=? AND rental_status IN ('Active','Overdue') AND end_date < ? ORDER BY end_date";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (branchId == null) {
                ps.setDate(1, Date.valueOf(today));
            } else {
                ps.setInt(1, branchId);
                ps.setDate(2, Date.valueOf(today));
            }
            List<Rental> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
            return list;
        }
    }

    @Override
    public void updateReturnDetails(Connection connection, Rental rental) throws SQLException {
        String sql = "UPDATE rental SET actual_return_date=?,late_fee=?,damage_charge=?,settlement_amount=?,payment_status=?,rental_status=? WHERE rental_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(rental.getActualReturnDate()));
            ps.setBigDecimal(2, rental.getLateFee());
            ps.setBigDecimal(3, rental.getDamageCharge());
            ps.setBigDecimal(4, rental.getSettlementAmount());
            ps.setString(5, rental.getPaymentStatus().toDbValue());
            ps.setString(6, rental.getRentalStatus().name());
            ps.setInt(7, rental.getRentalId());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateStatus(Connection connection, int rentalId, RentalStatus status) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE rental SET rental_status=? WHERE rental_id=?")) {
            ps.setString(1, status.name());
            ps.setInt(2, rentalId);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean existsOverlap(Connection connection, int equipmentId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT 1 FROM rental WHERE equipment_id=? AND rental_status IN ('Active','Overdue') AND start_date<=? AND end_date>=? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, equipmentId);
            ps.setDate(2, Date.valueOf(endDate));
            ps.setDate(3, Date.valueOf(startDate));
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    @Override
    public List<RevenueReportDTO> revenueReport(Connection connection, LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT b.branch_id,b.name,SUM(r.final_amount + COALESCE(r.late_fee,0) + COALESCE(r.damage_charge,0)) revenue "
            + "FROM rental r JOIN branch b ON b.branch_id=r.branch_id WHERE r.created_at BETWEEN ? AND ? GROUP BY b.branch_id,b.name ORDER BY b.branch_id";
        List<RevenueReportDTO> rows = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(toDate.plusDays(1).atStartOfDay().minusSeconds(1)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RevenueReportDTO row = new RevenueReportDTO();
                    row.branchId = rs.getInt("branch_id");
                    row.branchName = rs.getString("name");
                    row.revenue = rs.getBigDecimal("revenue");
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    @Override
    public List<UtilizationReportDTO> utilizationReport(Connection connection, LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT e.equipment_id, CONCAT(e.brand,' ',e.model) equipment_name, "
            + "COALESCE(SUM(DATEDIFF(LEAST(r.end_date, ?), GREATEST(r.start_date, ?)) + 1),0) rental_days "
            + "FROM equipment e LEFT JOIN rental r ON e.equipment_id=r.equipment_id AND r.rental_status IN ('Active','Returned','Overdue') "
            + "AND r.start_date<=? AND r.end_date>=? GROUP BY e.equipment_id,e.brand,e.model ORDER BY e.equipment_id";
        int reportPeriodDays = (int) (toDate.toEpochDay() - fromDate.toEpochDay() + 1);
        List<UtilizationReportDTO> rows = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(toDate));
            ps.setDate(2, Date.valueOf(fromDate));
            ps.setDate(3, Date.valueOf(toDate));
            ps.setDate(4, Date.valueOf(fromDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UtilizationReportDTO row = new UtilizationReportDTO();
                    row.equipmentId = rs.getInt("equipment_id");
                    row.equipmentName = rs.getString("equipment_name");
                    row.rentalDays = Math.max(0, rs.getInt("rental_days"));
                    row.availableDays = Math.max(0, reportPeriodDays - row.rentalDays);
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    private Rental map(ResultSet rs) throws SQLException {
        Rental r = new Rental();
        r.setRentalId(rs.getInt("rental_id"));
        r.setEquipmentId(rs.getInt("equipment_id"));
        r.setCustomerId(rs.getInt("customer_id"));
        r.setBranchId(rs.getInt("branch_id"));
        r.setStartDate(rs.getDate("start_date").toLocalDate());
        r.setEndDate(rs.getDate("end_date").toLocalDate());
        Date actual = rs.getDate("actual_return_date");
        r.setActualReturnDate(actual == null ? null : actual.toLocalDate());
        r.setRentalAmount(rs.getBigDecimal("rental_amount"));
        r.setSecurityDeposit(rs.getBigDecimal("security_deposit"));
        r.setMembershipDiscount(rs.getBigDecimal("membership_discount"));
        r.setLongRentalDiscount(rs.getBigDecimal("long_rental_discount"));
        r.setLateFee(rs.getBigDecimal("late_fee"));
        r.setDamageCharge(rs.getBigDecimal("damage_charge"));
        r.setFinalAmount(rs.getBigDecimal("final_amount"));
        r.setSettlementAmount(rs.getBigDecimal("settlement_amount"));
        r.setPaymentStatus(PaymentStatus.fromDb(rs.getString("payment_status")));
        r.setRentalStatus(RentalStatus.valueOf(rs.getString("rental_status")));
        return r;
    }
}

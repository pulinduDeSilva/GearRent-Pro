package edu.ijse.lk.dao.impl;

import edu.ijse.lk.dao.ReservationDAO;
import edu.ijse.lk.entity.Reservation;
import edu.ijse.lk.entity.ReservationStatus;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {
    @Override
    public int save(Connection connection, Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation(equipment_id,customer_id,branch_id,start_date,end_date,status) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reservation.getEquipmentId());
            ps.setInt(2, reservation.getCustomerId());
            ps.setInt(3, reservation.getBranchId());
            ps.setDate(4, Date.valueOf(reservation.getStartDate()));
            ps.setDate(5, Date.valueOf(reservation.getEndDate()));
            ps.setString(6, reservation.getStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    @Override
    public List<Reservation> findAll(Connection connection, Integer branchId) throws SQLException {
        String sql = branchId == null ? "SELECT * FROM reservation ORDER BY reservation_id DESC" : "SELECT * FROM reservation WHERE branch_id=? ORDER BY reservation_id DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (branchId != null) ps.setInt(1, branchId);
            List<Reservation> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
            return list;
        }
    }

    @Override
    public Reservation findById(Connection connection, int reservationId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservation WHERE reservation_id=?")) {
            ps.setInt(1, reservationId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    @Override
    public void updateStatus(Connection connection, int reservationId, ReservationStatus status) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE reservation SET status=? WHERE reservation_id=?")) {
            ps.setString(1, status.name());
            ps.setInt(2, reservationId);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean existsOverlap(Connection connection, int equipmentId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT 1 FROM reservation WHERE equipment_id=? AND status='Active' AND start_date<=? AND end_date>=? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, equipmentId);
            ps.setDate(2, Date.valueOf(endDate));
            ps.setDate(3, Date.valueOf(startDate));
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private Reservation map(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setReservationId(rs.getInt("reservation_id"));
        r.setEquipmentId(rs.getInt("equipment_id"));
        r.setCustomerId(rs.getInt("customer_id"));
        r.setBranchId(rs.getInt("branch_id"));
        r.setStartDate(rs.getDate("start_date").toLocalDate());
        r.setEndDate(rs.getDate("end_date").toLocalDate());
        r.setStatus(ReservationStatus.valueOf(rs.getString("status")));
        return r;
    }
}

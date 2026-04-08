package edu.ijse.lk.dao.impl;

import edu.ijse.lk.dao.EquipmentDAO;
import edu.ijse.lk.entity.Equipment;
import edu.ijse.lk.entity.EquipmentStatus;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAOImpl implements EquipmentDAO {
    @Override
    public List<Equipment> findAllByBranch(Connection connection, Integer branchId) throws SQLException {
        String sql = branchId == null ? "SELECT * FROM equipment ORDER BY equipment_id" : "SELECT * FROM equipment WHERE branch_id=? ORDER BY equipment_id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (branchId != null) ps.setInt(1, branchId);
            return mapList(ps);
        }
    }

    @Override
    public List<Equipment> search(Connection connection, Integer branchId, Integer categoryId, String brand, String model, String status) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM equipment WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (branchId != null) { sql.append(" AND branch_id=?"); params.add(branchId); }
        if (categoryId != null) { sql.append(" AND category_id=?"); params.add(categoryId); }
        if (brand != null && !brand.isBlank()) { sql.append(" AND brand LIKE ?"); params.add("%" + brand + "%"); }
        if (model != null && !model.isBlank()) { sql.append(" AND model LIKE ?"); params.add("%" + model + "%"); }
        if (status != null && !status.isBlank()) { sql.append(" AND status=?"); params.add(status); }
        sql.append(" ORDER BY equipment_id");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            return mapList(ps);
        }
    }

    @Override
    public Equipment findById(Connection connection, int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM equipment WHERE equipment_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    @Override
    public int save(Connection connection, Equipment equipment) throws SQLException {
        String sql = "INSERT INTO equipment(category_id,branch_id,brand,model,purchase_year,base_daily_price,security_deposit,status) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, equipment.getCategoryId());
            ps.setInt(2, equipment.getBranchId());
            ps.setString(3, equipment.getBrand());
            ps.setString(4, equipment.getModel());
            ps.setInt(5, equipment.getPurchaseYear());
            ps.setBigDecimal(6, equipment.getBaseDailyPrice());
            ps.setBigDecimal(7, equipment.getSecurityDeposit());
            ps.setString(8, equipment.getStatus().toDbValue());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    @Override
    public void update(Connection connection, Equipment equipment) throws SQLException {
        String sql = "UPDATE equipment SET category_id=?,branch_id=?,brand=?,model=?,purchase_year=?,base_daily_price=?,security_deposit=?,status=? WHERE equipment_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, equipment.getCategoryId());
            ps.setInt(2, equipment.getBranchId());
            ps.setString(3, equipment.getBrand());
            ps.setString(4, equipment.getModel());
            ps.setInt(5, equipment.getPurchaseYear());
            ps.setBigDecimal(6, equipment.getBaseDailyPrice());
            ps.setBigDecimal(7, equipment.getSecurityDeposit());
            ps.setString(8, equipment.getStatus().toDbValue());
            ps.setInt(9, equipment.getEquipmentId());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateStatus(Connection connection, int equipmentId, EquipmentStatus status) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE equipment SET status=? WHERE equipment_id=?")) {
            ps.setString(1, status.toDbValue());
            ps.setInt(2, equipmentId);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean hasRentalOrReservationConflict(Connection connection, int equipmentId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT 1 FROM reservation WHERE equipment_id=? AND status='Active' AND start_date<=? AND end_date>=? "
            + "UNION SELECT 1 FROM rental WHERE equipment_id=? AND rental_status IN ('Active','Overdue') AND start_date<=? AND end_date>=? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, equipmentId);
            ps.setDate(2, Date.valueOf(endDate));
            ps.setDate(3, Date.valueOf(startDate));
            ps.setInt(4, equipmentId);
            ps.setDate(5, Date.valueOf(endDate));
            ps.setDate(6, Date.valueOf(startDate));
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private List<Equipment> mapList(PreparedStatement ps) throws SQLException {
        List<Equipment> list = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private Equipment map(ResultSet rs) throws SQLException {
        Equipment e = new Equipment();
        e.setEquipmentId(rs.getInt("equipment_id"));
        e.setCategoryId(rs.getInt("category_id"));
        e.setBranchId(rs.getInt("branch_id"));
        e.setBrand(rs.getString("brand"));
        e.setModel(rs.getString("model"));
        e.setPurchaseYear(rs.getInt("purchase_year"));
        e.setBaseDailyPrice(rs.getBigDecimal("base_daily_price"));
        e.setSecurityDeposit(rs.getBigDecimal("security_deposit"));
        e.setStatus(EquipmentStatus.fromDb(rs.getString("status")));
        return e;
    }
}

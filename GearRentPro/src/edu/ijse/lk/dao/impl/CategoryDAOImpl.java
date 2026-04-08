package edu.ijse.lk.dao.impl;

import edu.ijse.lk.dao.CategoryDAO;
import edu.ijse.lk.entity.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    @Override
    public List<Category> findAll(Connection connection) throws SQLException {
        List<Category> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM category ORDER BY category_id"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public Category findById(Connection connection, int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM category WHERE category_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    @Override
    public int save(Connection connection, Category category) throws SQLException {
        String sql = "INSERT INTO category(name,description,price_factor,weekend_multiplier,late_fee_per_day,is_active) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setBigDecimal(3, category.getPriceFactor());
            ps.setBigDecimal(4, category.getWeekendMultiplier());
            ps.setBigDecimal(5, category.getLateFeePerDay());
            ps.setBoolean(6, category.isActive());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    @Override
    public void update(Connection connection, Category category) throws SQLException {
        String sql = "UPDATE category SET name=?,description=?,price_factor=?,weekend_multiplier=?,late_fee_per_day=?,is_active=? WHERE category_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setBigDecimal(3, category.getPriceFactor());
            ps.setBigDecimal(4, category.getWeekendMultiplier());
            ps.setBigDecimal(5, category.getLateFeePerDay());
            ps.setBoolean(6, category.isActive());
            ps.setInt(7, category.getCategoryId());
            ps.executeUpdate();
        }
    }

    @Override
    public void setActive(Connection connection, int id, boolean active) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE category SET is_active=? WHERE category_id=?")) {
            ps.setBoolean(1, active);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private Category map(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setCategoryId(rs.getInt("category_id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setPriceFactor(rs.getBigDecimal("price_factor"));
        c.setWeekendMultiplier(rs.getBigDecimal("weekend_multiplier"));
        c.setLateFeePerDay(rs.getBigDecimal("late_fee_per_day"));
        c.setActive(rs.getBoolean("is_active"));
        return c;
    }
}

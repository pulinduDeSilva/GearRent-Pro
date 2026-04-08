package edu.ijse.lk.dao.impl;

import edu.ijse.lk.dao.BranchDAO;
import edu.ijse.lk.entity.Branch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAOImpl implements BranchDAO {
    @Override
    public List<Branch> findAll(Connection connection) throws SQLException {
        List<Branch> list = new ArrayList<>();
        String sql = "SELECT branch_id, branch_code, name, address, contact FROM branch ORDER BY branch_id";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    @Override
    public Branch findById(Connection connection, int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT branch_id, branch_code, name, address, contact FROM branch WHERE branch_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public int save(Connection connection, Branch branch) throws SQLException {
        String sql = "INSERT INTO branch(branch_code,name,address,contact) VALUES(?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, branch.getBranchCode());
            ps.setString(2, branch.getName());
            ps.setString(3, branch.getAddress());
            ps.setString(4, branch.getContact());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    @Override
    public void update(Connection connection, Branch branch) throws SQLException {
        String sql = "UPDATE branch SET branch_code=?,name=?,address=?,contact=? WHERE branch_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, branch.getBranchCode());
            ps.setString(2, branch.getName());
            ps.setString(3, branch.getAddress());
            ps.setString(4, branch.getContact());
            ps.setInt(5, branch.getBranchId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Connection connection, int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Branch map(ResultSet rs) throws SQLException {
        return new Branch(rs.getInt("branch_id"), rs.getString("branch_code"), rs.getString("name"), rs.getString("address"), rs.getString("contact"));
    }
}

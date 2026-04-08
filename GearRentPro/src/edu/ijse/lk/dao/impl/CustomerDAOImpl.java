package edu.ijse.lk.dao.impl;

import edu.ijse.lk.dao.CustomerDAO;
import edu.ijse.lk.entity.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public List<Customer> findAll(Connection connection) throws SQLException {
        List<Customer> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM customer ORDER BY customer_id"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public Customer findById(Connection connection, int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM customer WHERE customer_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    @Override
    public int save(Connection connection, Customer customer) throws SQLException {
        String sql = "INSERT INTO customer(name,nic_passport,contact_no,email,address,membership_id) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getNicPassport());
            ps.setString(3, customer.getContactNo());
            ps.setString(4, customer.getEmail());
            ps.setString(5, customer.getAddress());
            if (customer.getMembershipId() == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, customer.getMembershipId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    @Override
    public void update(Connection connection, Customer customer) throws SQLException {
        String sql = "UPDATE customer SET name=?,nic_passport=?,contact_no=?,email=?,address=?,membership_id=? WHERE customer_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getNicPassport());
            ps.setString(3, customer.getContactNo());
            ps.setString(4, customer.getEmail());
            ps.setString(5, customer.getAddress());
            if (customer.getMembershipId() == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, customer.getMembershipId());
            ps.setInt(7, customer.getCustomerId());
            ps.executeUpdate();
        }
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getInt("customer_id"));
        c.setName(rs.getString("name"));
        c.setNicPassport(rs.getString("nic_passport"));
        c.setContactNo(rs.getString("contact_no"));
        c.setEmail(rs.getString("email"));
        c.setAddress(rs.getString("address"));
        int membershipId = rs.getInt("membership_id");
        c.setMembershipId(rs.wasNull() ? null : membershipId);
        return c;
    }
}

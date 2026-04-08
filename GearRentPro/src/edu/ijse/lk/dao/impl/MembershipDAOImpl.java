package edu.ijse.lk.dao.impl;

import edu.ijse.lk.dao.MembershipDAO;
import edu.ijse.lk.entity.Membership;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembershipDAOImpl implements MembershipDAO {
    @Override
    public List<Membership> findAll(Connection connection) throws SQLException {
        List<Membership> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM membership ORDER BY membership_id"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Membership m = new Membership();
                m.setMembershipId(rs.getInt("membership_id"));
                m.setLevelName(rs.getString("level_name"));
                m.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));
                list.add(m);
            }
        }
        return list;
    }

    @Override
    public Membership findById(Connection connection, int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM membership WHERE membership_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Membership m = new Membership();
                m.setMembershipId(rs.getInt("membership_id"));
                m.setLevelName(rs.getString("level_name"));
                m.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));
                return m;
            }
        }
    }
}

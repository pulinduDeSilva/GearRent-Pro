package edu.ijse.lk.dao.impl;

import edu.ijse.lk.dao.UserDAO;
import edu.ijse.lk.entity.Role;
import edu.ijse.lk.entity.SystemUser;
import java.sql.*;

public class UserDAOImpl implements UserDAO {
    @Override
    public SystemUser authenticate(Connection connection, String username, String password) throws SQLException {
        String sql = "SELECT user_id,username,password,role,branch_id FROM system_user WHERE username=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                SystemUser user = new SystemUser();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(Role.valueOf(rs.getString("role")));
                int branchId = rs.getInt("branch_id");
                user.setBranchId(rs.wasNull() ? null : branchId);
                return user;
            }
        }
    }
}

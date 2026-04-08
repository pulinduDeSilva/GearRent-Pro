package edu.ijse.lk.service;

import edu.ijse.lk.dao.UserDAO;
import edu.ijse.lk.dao.impl.UserDAOImpl;
import edu.ijse.lk.dto.UserDTO;
import edu.ijse.lk.entity.SystemUser;
import edu.ijse.lk.util.DBConnection;
import java.sql.Connection;

public class AuthService {
    private final UserDAO userDAO = new UserDAOImpl();

    public UserDTO login(String username, String password) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            SystemUser user = userDAO.authenticate(con, username, password);
            if (user == null) return null;
                boolean valid = password != null && password.equals(user.getPassword());
            if (!valid) return null;
            UserDTO dto = new UserDTO();
            dto.userId = user.getUserId();
            dto.username = user.getUsername();
            dto.role = user.getRole().name();
            dto.branchId = user.getBranchId();
            return dto;
        } catch (Exception ex) {
            throw new RuntimeException("Login failed: " + ex.getMessage(), ex);
        }
    }
}

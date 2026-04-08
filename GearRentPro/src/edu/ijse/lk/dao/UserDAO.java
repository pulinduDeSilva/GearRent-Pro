package edu.ijse.lk.dao;

import edu.ijse.lk.entity.SystemUser;
import java.sql.Connection;
import java.sql.SQLException;

public interface UserDAO {
    SystemUser authenticate(Connection connection, String username, String password) throws SQLException;
}

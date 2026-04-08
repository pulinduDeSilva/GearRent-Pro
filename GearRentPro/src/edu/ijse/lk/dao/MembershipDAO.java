package edu.ijse.lk.dao;

import edu.ijse.lk.entity.Membership;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface MembershipDAO {
    List<Membership> findAll(Connection connection) throws SQLException;
    Membership findById(Connection connection, int id) throws SQLException;
}

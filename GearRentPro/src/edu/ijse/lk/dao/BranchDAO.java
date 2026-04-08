package edu.ijse.lk.dao;

import edu.ijse.lk.entity.Branch;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BranchDAO {
    List<Branch> findAll(Connection connection) throws SQLException;
    Branch findById(Connection connection, int id) throws SQLException;
    int save(Connection connection, Branch branch) throws SQLException;
    void update(Connection connection, Branch branch) throws SQLException;
    void delete(Connection connection, int id) throws SQLException;
}

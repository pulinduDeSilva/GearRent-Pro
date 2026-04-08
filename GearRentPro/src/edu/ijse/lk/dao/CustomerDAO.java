package edu.ijse.lk.dao;

import edu.ijse.lk.entity.Customer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO {
    List<Customer> findAll(Connection connection) throws SQLException;
    Customer findById(Connection connection, int id) throws SQLException;
    int save(Connection connection, Customer customer) throws SQLException;
    void update(Connection connection, Customer customer) throws SQLException;
}

package edu.ijse.lk.dao;

import edu.ijse.lk.entity.Category;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {
    List<Category> findAll(Connection connection) throws SQLException;
    Category findById(Connection connection, int id) throws SQLException;
    int save(Connection connection, Category category) throws SQLException;
    void update(Connection connection, Category category) throws SQLException;
    void setActive(Connection connection, int id, boolean active) throws SQLException;
}

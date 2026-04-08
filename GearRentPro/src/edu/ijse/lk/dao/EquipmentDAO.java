package edu.ijse.lk.dao;

import edu.ijse.lk.entity.Equipment;
import edu.ijse.lk.entity.EquipmentStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface EquipmentDAO {
    List<Equipment> findAllByBranch(Connection connection, Integer branchId) throws SQLException;
    List<Equipment> search(Connection connection, Integer branchId, Integer categoryId, String brand, String model, String status) throws SQLException;
    Equipment findById(Connection connection, int id) throws SQLException;
    int save(Connection connection, Equipment equipment) throws SQLException;
    void update(Connection connection, Equipment equipment) throws SQLException;
    void updateStatus(Connection connection, int equipmentId, EquipmentStatus status) throws SQLException;
    boolean hasRentalOrReservationConflict(Connection connection, int equipmentId, LocalDate startDate, LocalDate endDate) throws SQLException;
}

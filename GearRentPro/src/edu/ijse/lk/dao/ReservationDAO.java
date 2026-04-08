package edu.ijse.lk.dao;

import edu.ijse.lk.entity.Reservation;
import edu.ijse.lk.entity.ReservationStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {
    int save(Connection connection, Reservation reservation) throws SQLException;
    List<Reservation> findAll(Connection connection, Integer branchId) throws SQLException;
    Reservation findById(Connection connection, int reservationId) throws SQLException;
    void updateStatus(Connection connection, int reservationId, ReservationStatus status) throws SQLException;
    boolean existsOverlap(Connection connection, int equipmentId, LocalDate startDate, LocalDate endDate) throws SQLException;
}

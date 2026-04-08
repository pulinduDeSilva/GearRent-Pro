package edu.ijse.lk.dao;

import edu.ijse.lk.dto.RevenueReportDTO;
import edu.ijse.lk.dto.UtilizationReportDTO;
import edu.ijse.lk.entity.Rental;
import edu.ijse.lk.entity.RentalStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RentalDAO {
    int save(Connection connection, Rental rental) throws SQLException;
    Rental findById(Connection connection, int rentalId) throws SQLException;
    List<Rental> findAll(Connection connection, Integer branchId) throws SQLException;
    List<Rental> findOverdue(Connection connection, Integer branchId, LocalDate today) throws SQLException;
    void updateReturnDetails(Connection connection, Rental rental) throws SQLException;
    void updateStatus(Connection connection, int rentalId, RentalStatus status) throws SQLException;
    boolean existsOverlap(Connection connection, int equipmentId, LocalDate startDate, LocalDate endDate) throws SQLException;
    List<RevenueReportDTO> revenueReport(Connection connection, LocalDate fromDate, LocalDate toDate) throws SQLException;
    List<UtilizationReportDTO> utilizationReport(Connection connection, LocalDate fromDate, LocalDate toDate) throws SQLException;
}

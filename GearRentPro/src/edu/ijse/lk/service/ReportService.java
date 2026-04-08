package edu.ijse.lk.service;

import edu.ijse.lk.dao.RentalDAO;
import edu.ijse.lk.dao.impl.RentalDAOImpl;
import edu.ijse.lk.dto.RevenueReportDTO;
import edu.ijse.lk.dto.UtilizationReportDTO;
import edu.ijse.lk.util.DBConnection;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class ReportService {
    private final RentalDAO rentalDAO = new RentalDAOImpl();

    public List<RevenueReportDTO> branchRevenue(LocalDate fromDate, LocalDate toDate) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            return rentalDAO.revenueReport(con, fromDate, toDate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<UtilizationReportDTO> utilization(LocalDate fromDate, LocalDate toDate) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            return rentalDAO.utilizationReport(con, fromDate, toDate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

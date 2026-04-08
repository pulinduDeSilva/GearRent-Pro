package edu.ijse.lk.controller;

import edu.ijse.lk.dto.RevenueReportDTO;
import edu.ijse.lk.dto.UtilizationReportDTO;
import edu.ijse.lk.service.ReportService;
import java.time.LocalDate;
import java.util.List;

public class ReportController {
    private final ReportService service = new ReportService();

    public List<RevenueReportDTO> revenue(LocalDate fromDate, LocalDate toDate) { return service.branchRevenue(fromDate, toDate); }
    public List<UtilizationReportDTO> utilization(LocalDate fromDate, LocalDate toDate) { return service.utilization(fromDate, toDate); }
}

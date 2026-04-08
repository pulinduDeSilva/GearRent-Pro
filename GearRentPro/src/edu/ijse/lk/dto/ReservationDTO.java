package edu.ijse.lk.dto;

import java.time.LocalDate;

public class ReservationDTO {
    public int reservationId;
    public int equipmentId;
    public int customerId;
    public int branchId;
    public LocalDate startDate;
    public LocalDate endDate;
    public String status;
}

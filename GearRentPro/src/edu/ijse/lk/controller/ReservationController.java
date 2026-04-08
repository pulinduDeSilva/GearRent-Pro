package edu.ijse.lk.controller;

import edu.ijse.lk.dto.ReservationDTO;
import edu.ijse.lk.service.ReservationService;
import java.util.List;

public class ReservationController {
    private final ReservationService service = new ReservationService();

    public int create(ReservationDTO dto) { return service.create(dto); }
    public int convertToRental(int reservationId) { return service.convertToRental(reservationId); }
    public List<ReservationDTO> list(Integer branchId) { return service.list(branchId); }
    public void cancel(int reservationId) { service.cancel(reservationId); }
}

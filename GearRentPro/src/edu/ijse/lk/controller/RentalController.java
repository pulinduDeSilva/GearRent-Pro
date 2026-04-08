package edu.ijse.lk.controller;

import edu.ijse.lk.dto.RentalDTO;
import edu.ijse.lk.service.RentalService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RentalController {
    private final RentalService service = new RentalService();

    public int create(RentalDTO dto) { return service.create(dto); }
    public void returnRental(int rentalId, LocalDate actualReturnDate, BigDecimal damageCharge) { service.returnRental(rentalId, actualReturnDate, damageCharge); }
    public List<RentalDTO> list(Integer branchId) { return service.list(branchId); }
    public List<RentalDTO> overdue(Integer branchId) { return service.overdue(branchId); }
}

package edu.ijse.lk.controller;

import edu.ijse.lk.dto.CustomerDTO;
import edu.ijse.lk.service.CustomerService;
import java.util.List;

public class CustomerController {
    private final CustomerService service = new CustomerService();

    public List<CustomerDTO> list() { return service.list(); }
    public int create(CustomerDTO dto) { return service.create(dto); }
    public void update(CustomerDTO dto) { service.update(dto); }
}

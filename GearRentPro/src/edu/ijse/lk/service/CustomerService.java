package edu.ijse.lk.service;

import edu.ijse.lk.dao.CustomerDAO;
import edu.ijse.lk.dao.impl.CustomerDAOImpl;
import edu.ijse.lk.dto.CustomerDTO;
import edu.ijse.lk.entity.Customer;
import edu.ijse.lk.util.DBConnection;
import edu.ijse.lk.util.ValidationUtil;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private final CustomerDAO dao = new CustomerDAOImpl();

    public List<CustomerDTO> list() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            List<CustomerDTO> out = new ArrayList<>();
            for (Customer c : dao.findAll(con)) out.add(toDTO(c));
            return out;
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public int create(CustomerDTO dto) {
        validate(dto);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            return dao.save(con, toEntity(dto));
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public void update(CustomerDTO dto) {
        ValidationUtil.require(dto.customerId > 0, "Customer ID required.");
        validate(dto);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            dao.update(con, toEntity(dto));
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    private void validate(CustomerDTO dto) {
        ValidationUtil.require(dto.name != null && !dto.name.isBlank(), "Customer name is required.");
        ValidationUtil.require(dto.nicPassport != null && !dto.nicPassport.isBlank(), "NIC/Passport is required.");
    }

    private Customer toEntity(CustomerDTO dto) {
        Customer c = new Customer();
        c.setCustomerId(dto.customerId);
        c.setName(dto.name);
        c.setNicPassport(dto.nicPassport);
        c.setContactNo(dto.contactNo);
        c.setEmail(dto.email);
        c.setAddress(dto.address);
        c.setMembershipId(dto.membershipId);
        return c;
    }

    private CustomerDTO toDTO(Customer c) {
        CustomerDTO dto = new CustomerDTO();
        dto.customerId = c.getCustomerId();
        dto.name = c.getName();
        dto.nicPassport = c.getNicPassport();
        dto.contactNo = c.getContactNo();
        dto.email = c.getEmail();
        dto.address = c.getAddress();
        dto.membershipId = c.getMembershipId();
        return dto;
    }
}

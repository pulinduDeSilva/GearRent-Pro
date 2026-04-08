package edu.ijse.lk.service;

import edu.ijse.lk.dao.BranchDAO;
import edu.ijse.lk.dao.impl.BranchDAOImpl;
import edu.ijse.lk.dto.BranchDTO;
import edu.ijse.lk.entity.Branch;
import edu.ijse.lk.util.DBConnection;
import edu.ijse.lk.util.ValidationUtil;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class BranchService {
    private final BranchDAO dao = new BranchDAOImpl();

    public List<BranchDTO> list() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            List<BranchDTO> out = new ArrayList<>();
            for (Branch branch : dao.findAll(con)) out.add(toDTO(branch));
            return out;
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public int create(BranchDTO dto) {
        validate(dto);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            return dao.save(con, toEntity(dto));
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public void update(BranchDTO dto) {
        ValidationUtil.require(dto.branchId > 0, "Branch ID is required.");
        validate(dto);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            dao.update(con, toEntity(dto));
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public void delete(int branchId) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            dao.delete(con, branchId);
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    private void validate(BranchDTO dto) {
        ValidationUtil.require(dto.branchCode != null && !dto.branchCode.isBlank(), "Branch code is required.");
        ValidationUtil.require(dto.name != null && !dto.name.isBlank(), "Branch name is required.");
    }

    private Branch toEntity(BranchDTO dto) {
        return new Branch(dto.branchId, dto.branchCode, dto.name, dto.address, dto.contact);
    }

    private BranchDTO toDTO(Branch e) {
        BranchDTO dto = new BranchDTO();
        dto.branchId = e.getBranchId();
        dto.branchCode = e.getBranchCode();
        dto.name = e.getName();
        dto.address = e.getAddress();
        dto.contact = e.getContact();
        return dto;
    }
}

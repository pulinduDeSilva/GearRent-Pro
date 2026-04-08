package edu.ijse.lk.service;

import edu.ijse.lk.dao.EquipmentDAO;
import edu.ijse.lk.dao.impl.EquipmentDAOImpl;
import edu.ijse.lk.dto.EquipmentDTO;
import edu.ijse.lk.entity.Equipment;
import edu.ijse.lk.entity.EquipmentStatus;
import edu.ijse.lk.util.DBConnection;
import edu.ijse.lk.util.ValidationUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class EquipmentService {
    private final EquipmentDAO dao = new EquipmentDAOImpl();

    public List<EquipmentDTO> list(Integer branchId) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            List<EquipmentDTO> out = new ArrayList<>();
            for (Equipment e : dao.findAllByBranch(con, branchId)) out.add(toDTO(e));
            return out;
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public List<EquipmentDTO> search(Integer branchId, Integer categoryId, String brand, String model, String status) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            List<EquipmentDTO> out = new ArrayList<>();
            for (Equipment e : dao.search(con, branchId, categoryId, brand, model, status)) out.add(toDTO(e));
            return out;
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public int create(EquipmentDTO dto) {
        validate(dto);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            return dao.save(con, toEntity(dto));
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public void update(EquipmentDTO dto) {
        ValidationUtil.require(dto.equipmentId > 0, "Equipment ID required.");
        validate(dto);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            dao.update(con, toEntity(dto));
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    private void validate(EquipmentDTO dto) {
        ValidationUtil.require(dto.categoryId > 0, "Category is required.");
        ValidationUtil.require(dto.branchId > 0, "Branch is required.");
        ValidationUtil.require(dto.brand != null && !dto.brand.isBlank(), "Brand is required.");
        ValidationUtil.require(dto.model != null && !dto.model.isBlank(), "Model is required.");
        ValidationUtil.require(dto.baseDailyPrice != null && dto.baseDailyPrice.compareTo(BigDecimal.ZERO) > 0, "Base daily price must be > 0");
        ValidationUtil.require(dto.securityDeposit != null && dto.securityDeposit.compareTo(BigDecimal.ZERO) >= 0, "Security deposit must be >= 0");
    }

    private Equipment toEntity(EquipmentDTO dto) {
        Equipment e = new Equipment();
        e.setEquipmentId(dto.equipmentId);
        e.setCategoryId(dto.categoryId);
        e.setBranchId(dto.branchId);
        e.setBrand(dto.brand);
        e.setModel(dto.model);
        e.setPurchaseYear(dto.purchaseYear);
        e.setBaseDailyPrice(dto.baseDailyPrice);
        e.setSecurityDeposit(dto.securityDeposit);
        e.setStatus(dto.status == null ? EquipmentStatus.Available : EquipmentStatus.fromDb(dto.status));
        return e;
    }

    private EquipmentDTO toDTO(Equipment e) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.equipmentId = e.getEquipmentId();
        dto.categoryId = e.getCategoryId();
        dto.branchId = e.getBranchId();
        dto.brand = e.getBrand();
        dto.model = e.getModel();
        dto.purchaseYear = e.getPurchaseYear();
        dto.baseDailyPrice = e.getBaseDailyPrice();
        dto.securityDeposit = e.getSecurityDeposit();
        dto.status = e.getStatus().toDbValue();
        return dto;
    }
}

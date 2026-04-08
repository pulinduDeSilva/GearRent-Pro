package edu.ijse.lk.controller;

import edu.ijse.lk.dto.EquipmentDTO;
import edu.ijse.lk.service.EquipmentService;
import java.util.List;

public class EquipmentController {
    private final EquipmentService service = new EquipmentService();

    public List<EquipmentDTO> list(Integer branchId) { return service.list(branchId); }
    public List<EquipmentDTO> search(Integer branchId, Integer categoryId, String brand, String model, String status) {
        return service.search(branchId, categoryId, brand, model, status);
    }
    public int create(EquipmentDTO dto) { return service.create(dto); }
    public void update(EquipmentDTO dto) { service.update(dto); }
}

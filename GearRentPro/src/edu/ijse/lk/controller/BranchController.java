package edu.ijse.lk.controller;

import edu.ijse.lk.dto.BranchDTO;
import edu.ijse.lk.service.BranchService;
import java.util.List;

public class BranchController {
    private final BranchService service = new BranchService();

    public List<BranchDTO> list() { return service.list(); }
    public int create(BranchDTO dto) { return service.create(dto); }
    public void update(BranchDTO dto) { service.update(dto); }
    public void delete(int id) { service.delete(id); }
}

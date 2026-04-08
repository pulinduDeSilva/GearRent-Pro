package edu.ijse.lk.controller;

import edu.ijse.lk.dto.CategoryDTO;
import edu.ijse.lk.service.CategoryService;
import java.util.List;

public class CategoryController {
    private final CategoryService service = new CategoryService();

    public List<CategoryDTO> list() { return service.list(); }
    public int create(CategoryDTO dto) { return service.create(dto); }
    public void update(CategoryDTO dto) { service.update(dto); }
    public void setActive(int id, boolean active) { service.setActive(id, active); }
}

package edu.ijse.lk.service;

import edu.ijse.lk.dao.CategoryDAO;
import edu.ijse.lk.dao.impl.CategoryDAOImpl;
import edu.ijse.lk.dto.CategoryDTO;
import edu.ijse.lk.entity.Category;
import edu.ijse.lk.util.DBConnection;
import edu.ijse.lk.util.ValidationUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private final CategoryDAO dao = new CategoryDAOImpl();

    public List<CategoryDTO> list() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            List<CategoryDTO> out = new ArrayList<>();
            for (Category c : dao.findAll(con)) out.add(toDTO(c));
            return out;
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public int create(CategoryDTO dto) {
        validate(dto);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            return dao.save(con, toEntity(dto));
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public void update(CategoryDTO dto) {
        ValidationUtil.require(dto.categoryId > 0, "Category ID required.");
        validate(dto);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            dao.update(con, toEntity(dto));
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public void setActive(int id, boolean active) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            dao.setActive(con, id, active);
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    private void validate(CategoryDTO dto) {
        ValidationUtil.require(dto.name != null && !dto.name.isBlank(), "Category name is required.");
        ValidationUtil.require(dto.priceFactor != null && dto.priceFactor.compareTo(BigDecimal.ZERO) > 0, "Price factor must be > 0");
        ValidationUtil.require(dto.weekendMultiplier != null && dto.weekendMultiplier.compareTo(BigDecimal.ZERO) > 0, "Weekend multiplier must be > 0");
        ValidationUtil.require(dto.lateFeePerDay != null && dto.lateFeePerDay.compareTo(BigDecimal.ZERO) >= 0, "Late fee must be >= 0");
    }

    private Category toEntity(CategoryDTO dto) {
        Category c = new Category();
        c.setCategoryId(dto.categoryId);
        c.setName(dto.name);
        c.setDescription(dto.description);
        c.setPriceFactor(dto.priceFactor);
        c.setWeekendMultiplier(dto.weekendMultiplier);
        c.setLateFeePerDay(dto.lateFeePerDay);
        c.setActive(dto.active);
        return c;
    }

    private CategoryDTO toDTO(Category c) {
        CategoryDTO dto = new CategoryDTO();
        dto.categoryId = c.getCategoryId();
        dto.name = c.getName();
        dto.description = c.getDescription();
        dto.priceFactor = c.getPriceFactor();
        dto.weekendMultiplier = c.getWeekendMultiplier();
        dto.lateFeePerDay = c.getLateFeePerDay();
        dto.active = c.isActive();
        return dto;
    }
}

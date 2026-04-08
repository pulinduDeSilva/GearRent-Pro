package edu.ijse.lk.entity;

public enum EquipmentStatus {
    Available,
    Reserved,
    Rented,
    Under_Maintenance;

    public static EquipmentStatus fromDb(String value) {
        return "Under Maintenance".equalsIgnoreCase(value) ? Under_Maintenance : EquipmentStatus.valueOf(value);
    }

    public String toDbValue() {
        return this == Under_Maintenance ? "Under Maintenance" : name();
    }
}

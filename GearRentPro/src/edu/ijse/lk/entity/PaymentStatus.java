package edu.ijse.lk.entity;

public enum PaymentStatus {
    Paid,
    Partially_Paid,
    Unpaid;

    public static PaymentStatus fromDb(String value) {
        return "Partially Paid".equalsIgnoreCase(value) ? Partially_Paid : PaymentStatus.valueOf(value);
    }

    public String toDbValue() {
        return this == Partially_Paid ? "Partially Paid" : name();
    }
}

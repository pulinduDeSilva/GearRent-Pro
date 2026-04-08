package edu.ijse.lk.entity;

public class Branch {
    private int branchId;
    private String branchCode;
    private String name;
    private String address;
    private String contact;

    public Branch() {}

    public Branch(int branchId, String branchCode, String name, String address, String contact) {
        this.branchId = branchId;
        this.branchCode = branchCode;
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }
    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() { return branchId + " - " + name; }
}

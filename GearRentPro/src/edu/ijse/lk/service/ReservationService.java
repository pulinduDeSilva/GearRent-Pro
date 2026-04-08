package edu.ijse.lk.service;

import edu.ijse.lk.dao.EquipmentDAO;
import edu.ijse.lk.dao.ReservationDAO;
import edu.ijse.lk.dao.RentalDAO;
import edu.ijse.lk.dao.impl.EquipmentDAOImpl;
import edu.ijse.lk.dao.impl.ReservationDAOImpl;
import edu.ijse.lk.dao.impl.RentalDAOImpl;
import edu.ijse.lk.dto.ReservationDTO;
import edu.ijse.lk.dto.RentalDTO;
import edu.ijse.lk.entity.EquipmentStatus;
import edu.ijse.lk.entity.Reservation;
import edu.ijse.lk.entity.ReservationStatus;
import edu.ijse.lk.entity.Rental;
import edu.ijse.lk.entity.RentalStatus;
import edu.ijse.lk.util.DBConnection;
import edu.ijse.lk.util.ValidationUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final EquipmentDAO equipmentDAO = new EquipmentDAOImpl();
    private final RentalDAO rentalDAO = new RentalDAOImpl();
    private final RentalService rentalService = new RentalService();

    public int create(ReservationDTO dto) {
        ValidationUtil.validateDateRange(dto.startDate, dto.endDate);
        try {
            Connection con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            try {
                ValidationUtil.require(!reservationDAO.existsOverlap(con, dto.equipmentId, dto.startDate, dto.endDate), "Overlapping reservation exists.");
                ValidationUtil.require(!rentalDAO.existsOverlap(con, dto.equipmentId, dto.startDate, dto.endDate), "Overlapping rental exists.");

                Reservation reservation = new Reservation();
                reservation.setEquipmentId(dto.equipmentId);
                reservation.setCustomerId(dto.customerId);
                reservation.setBranchId(dto.branchId);
                reservation.setStartDate(dto.startDate);
                reservation.setEndDate(dto.endDate);
                reservation.setStatus(ReservationStatus.Active);
                int id = reservationDAO.save(con, reservation);
                equipmentDAO.updateStatus(con, dto.equipmentId, EquipmentStatus.Reserved);
                con.commit();
                return id;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create reservation: " + ex.getMessage(), ex);
        }
    }

    public int convertToRental(int reservationId) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            try {
                Reservation reservation = reservationDAO.findById(con, reservationId);
                ValidationUtil.require(reservation != null, "Reservation not found.");
                ValidationUtil.require(reservation.getStatus() == ReservationStatus.Active, "Reservation is not active.");

                RentalDTO dto = new RentalDTO();
                dto.equipmentId = reservation.getEquipmentId();
                dto.customerId = reservation.getCustomerId();
                dto.branchId = reservation.getBranchId();
                dto.startDate = reservation.getStartDate();
                dto.endDate = reservation.getEndDate();
                dto.securityDeposit = BigDecimal.ZERO;
                int rentalId = rentalService.createInternal(con, dto);
                reservationDAO.updateStatus(con, reservationId, ReservationStatus.Converted);
                equipmentDAO.updateStatus(con, reservation.getEquipmentId(), EquipmentStatus.Rented);
                con.commit();
                return rentalId;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to convert reservation: " + ex.getMessage(), ex);
        }
    }

    public List<ReservationDTO> list(Integer branchId) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            List<ReservationDTO> out = new ArrayList<>();
            for (Reservation r : reservationDAO.findAll(con, branchId)) {
                ReservationDTO dto = new ReservationDTO();
                dto.reservationId = r.getReservationId();
                dto.equipmentId = r.getEquipmentId();
                dto.customerId = r.getCustomerId();
                dto.branchId = r.getBranchId();
                dto.startDate = r.getStartDate();
                dto.endDate = r.getEndDate();
                dto.status = r.getStatus().name();
                out.add(dto);
            }
            return out;
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    public void cancel(int reservationId) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            try {
                Reservation reservation = reservationDAO.findById(con, reservationId);
                ValidationUtil.require(reservation != null, "Reservation not found.");
                reservationDAO.updateStatus(con, reservationId, ReservationStatus.Cancelled);
                equipmentDAO.updateStatus(con, reservation.getEquipmentId(), EquipmentStatus.Available);
                con.commit();
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

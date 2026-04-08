package edu.ijse.lk.service;

import edu.ijse.lk.dao.MembershipDAO;
import edu.ijse.lk.dao.impl.MembershipDAOImpl;
import edu.ijse.lk.dto.MembershipDTO;
import edu.ijse.lk.entity.Membership;
import edu.ijse.lk.util.DBConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MembershipService {
    private final MembershipDAO dao = new MembershipDAOImpl();

    public List<MembershipDTO> list() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            List<MembershipDTO> out = new ArrayList<>();
            for (Membership m : dao.findAll(con)) {
                MembershipDTO dto = new MembershipDTO();
                dto.membershipId = m.getMembershipId();
                dto.levelName = m.getLevelName();
                dto.discountPercentage = m.getDiscountPercentage();
                out.add(dto);
            }
            return out;
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }
}

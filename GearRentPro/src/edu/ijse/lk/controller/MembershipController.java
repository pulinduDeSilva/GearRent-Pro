package edu.ijse.lk.controller;

import edu.ijse.lk.dto.MembershipDTO;
import edu.ijse.lk.service.MembershipService;
import java.util.List;

public class MembershipController {
    private final MembershipService service = new MembershipService();

    public List<MembershipDTO> list() { return service.list(); }
}

package edu.ijse.lk.controller;

import edu.ijse.lk.dto.UserDTO;
import edu.ijse.lk.service.AuthService;

public class AuthController {
    private final AuthService service = new AuthService();

    public UserDTO login(String username, String password) {
        return service.login(username, password);
    }
}

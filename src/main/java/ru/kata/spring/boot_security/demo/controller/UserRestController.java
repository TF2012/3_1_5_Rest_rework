package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    private final UserService service;

    @Autowired
    public UserRestController(UserService service) { this.service = service; }

    @GetMapping("/showAccount")
    public ResponseEntity<User> showUserAccount(Principal principal) {
        User user = service.getUserByUsername(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

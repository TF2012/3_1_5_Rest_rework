package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("showAccount")
    public ResponseEntity<User> showInfoUSer(Principal principal) {
        return ResponseEntity.ok(userService.getUserByUsername(principal.getName()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Collection<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Collection<Role>> getRole(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.getRoles(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") Long id) {
        if (userService.getUserById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.editUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        if (userService.getUserById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

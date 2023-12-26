package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("The user with this username not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getRoles().stream().map(
                        role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList()));
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    public Optional<User> getById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Transactional
    public void updateUser(Long id, User newUser) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            user.get().setUsername(newUser.getUsername());
            user.get().setPassword(encoder.encode(newUser.getPassword()));
            user.get().setEmail(newUser.getEmail());
            user.get().setRoles(newUser.getRoles());
            repository.save(user.get());
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        Optional<User> user = repository.findById(id);
        user.ifPresent(repository::delete);
    }
}

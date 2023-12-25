package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.List;
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
        User user_from_DB = repository.getUserByUsername(username).get();
        return new org.springframework.security.core.userdetails.User(
                user_from_DB.getUsername(),
                user_from_DB.getPassword(),
                mapRolesToAuthorities(user_from_DB.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User getUserByUsername(String name) {
        if (repository.getUserByUsername(name).isEmpty()) {
            throw new UsernameNotFoundException("not found user name");
        }
        return repository.getUserByUsername(name).get();
    }

    public User getUserById(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("not found user id");
        }
        return repository.findById(id).get();
    }

    @Transactional
    public void editUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Transactional
    public void addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Transactional
    public boolean deleteUserById(Long id) {
        if (repository.findById(id).isEmpty()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }


}

package com.coursemanager.service;

import com.coursemanager.dto.UserRegistrationDto;
import com.coursemanager.exception.BusinessException;
import com.coursemanager.model.entity.User;
import com.coursemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private static final Set<String> ALLOWED_ROLES = Set.of("ROLE_LIMITED_USER", "ROLE_FULL_USER", "ROLE_ADMIN");

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    public void registerNewUser(UserRegistrationDto registrationDto) {
        String username = registrationDto.getUsername().trim().toLowerCase();
        String email = registrationDto.getEmail().trim().toLowerCase();
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Username already exists");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException("Email already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(email);
        user.setRole("ROLE_LIMITED_USER");
        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));
    }

    public void changeUserRole(Long userId, String newRole) {
        if (!ALLOWED_ROLES.contains(newRole)) {
            throw new BusinessException("Nieprawidłowa rola użytkownika");
        }
        User user = findUserById(userId);
        user.setRole(newRole);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public long countUsers() {
        return userRepository.count();
    }
}

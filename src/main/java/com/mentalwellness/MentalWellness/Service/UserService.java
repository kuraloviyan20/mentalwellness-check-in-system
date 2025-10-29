package com.mentalwellness.MentalWellness.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentalwellness.MentalWellness.model.User;
import com.mentalwellness.MentalWellness.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        try {
            validateUser(user);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error creating user. Email might be already in use.", e);
        }
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        validateUser(userDetails);
        
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setContact_no(userDetails.getContact_no());
        user.setJoin_date(userDetails.getJoin_date());

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error updating user. Email might be already in use.", e);
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }
        if (user.getContact_no() == null || user.getContact_no().trim().isEmpty()) {
            throw new IllegalArgumentException("User contact number cannot be empty");
        }
        if (user.getJoin_date() == null || user.getJoin_date().trim().isEmpty()) {
            throw new IllegalArgumentException("User join date cannot be empty");
        }
    }
}
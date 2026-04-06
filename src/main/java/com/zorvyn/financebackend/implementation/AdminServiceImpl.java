package com.zorvyn.financebackend.implementation;




import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zorvyn.financebackend.dto.request.UpdateUserRequest;
import com.zorvyn.financebackend.dto.response.UserResponse;
import com.zorvyn.financebackend.entity.User;
import com.zorvyn.financebackend.exception.ResourceNotFoundException;
import com.zorvyn.financebackend.repository.UserRepository;
import com.zorvyn.financebackend.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return UserResponse.from(findUser(id));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = findUser(id);

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getIsActive() != null) {
            user.setActive(request.getIsActive());
        }

        return UserResponse.from(userRepository.save(user));
    }

    private User findUser(Long id) {
    	return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
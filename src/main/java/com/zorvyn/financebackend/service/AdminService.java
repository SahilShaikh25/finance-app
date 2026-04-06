package com.zorvyn.financebackend.service;


import java.util.List;

import com.zorvyn.financebackend.dto.request.UpdateUserRequest;
import com.zorvyn.financebackend.dto.response.UserResponse;

public interface AdminService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UpdateUserRequest request);
}

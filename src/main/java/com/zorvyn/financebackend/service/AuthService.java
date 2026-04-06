package com.zorvyn.financebackend.service;

import com.zorvyn.financebackend.dto.request.LoginRequest;
import com.zorvyn.financebackend.dto.request.RegisterRequest;
import com.zorvyn.financebackend.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
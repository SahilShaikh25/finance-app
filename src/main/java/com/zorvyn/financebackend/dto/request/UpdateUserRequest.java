package com.zorvyn.financebackend.dto.request;


import com.zorvyn.financebackend.enums.Role;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private Role role;
    private Boolean isActive;
}

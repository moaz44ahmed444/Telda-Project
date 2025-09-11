package com.telda.telda_project.dto;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String name;
    private String email;
    private String phone;
}

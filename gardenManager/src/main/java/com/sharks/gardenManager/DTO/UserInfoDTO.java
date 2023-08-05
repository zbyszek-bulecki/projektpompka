package com.sharks.gardenManager.DTO;

import com.sharks.gardenManager.entities.User;

public record UserInfoDTO(String username, User.Role role) {
}

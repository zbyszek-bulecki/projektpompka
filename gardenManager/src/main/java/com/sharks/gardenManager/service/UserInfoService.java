package com.sharks.gardenManager.service;

import com.sharks.gardenManager.DTO.UserInfoDTO;
import com.sharks.gardenManager.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    UserRepository userRepository;

    public UserInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfoDTO getUserInfo(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserInfoDTO(user.getUsername(), user.getRole()))
                .orElseThrow();
    }
}

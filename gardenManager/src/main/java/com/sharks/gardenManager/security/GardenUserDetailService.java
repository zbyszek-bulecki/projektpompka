package com.sharks.gardenManager.security;

import com.sharks.gardenManager.entities.User;
import com.sharks.gardenManager.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GardenUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public GardenUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        return GardenUserDetails.of(user.get());
    }
}

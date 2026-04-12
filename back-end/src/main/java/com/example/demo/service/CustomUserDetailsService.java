package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.GetCurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Users user = userRepository.findByUsername(username).get();
       if(user == null){
           throw new UsernameNotFoundException("User not found");
       }
       UserDetails userDetails = User.builder()
               .username(user.getUsername())
               .password(user.getPassword())
               .roles(user.getRoles().name())
               .build();
       return userDetails;
    }
}

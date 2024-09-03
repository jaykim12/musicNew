package com.example.demo.Global.security;

import com.example.demo.Entity.User;
import com.example.demo.Global.exception.CustomException;
import com.example.demo.Global.exception.ErrorCode;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_ID)
        );
//        return new com.example.demo.global.security.UserDetailsImpl(user, user.getUserId());
        return new UserDetailsImpl(user,user.getUserId());
    }
}
package com.example.project.service;

import com.example.project.Model.ApplicationUser;
import com.example.project.Model.MyUserPrincipal;
import com.example.project.repository.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private ApplicationUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("Finding details for {}", username);
        ;
        if(repository.findById(username).get().isPresent()){
            return new MyUserPrincipal(repository.findById(username).get().getUser_name(), user.get().getPassword());
        }
        return null;
    }
}

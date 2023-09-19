package ru.yakaska.tengen.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.yakaska.tengen.entity.User;
import ru.yakaska.tengen.repository.UserRepository;

import java.util.Collections;
import java.util.Set;

@Service
public class TengenUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public TengenUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).get();
        Set<GrantedAuthority> authorities = Collections.emptySet();
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );

    }
}

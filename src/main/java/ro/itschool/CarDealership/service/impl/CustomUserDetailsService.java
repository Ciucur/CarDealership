package ro.itschool.CarDealership.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ro.itschool.CarDealership.entity.MyUser;
import ro.itschool.CarDealership.entity.Role;
import ro.itschool.CarDealership.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
//    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = userService.findUserByUserName(username);
        List<GrantedAuthority> authorities = getUserAuthority(myUser.getRoles());
        return new MyUser(myUser.getUsername(), myUser.getPassword(),
                myUser.isEnabled(), myUser.isAccountNonExpired(), myUser.isCredentialsNonExpired(), myUser.isAccountNonLocked(), authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new ArrayList<>(roles);
    }
}

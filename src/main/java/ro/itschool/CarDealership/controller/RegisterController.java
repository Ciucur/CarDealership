package ro.itschool.CarDealership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ro.itschool.CarDealership.entity.MyUser;
import ro.itschool.CarDealership.repository.RoleRepository;
import ro.itschool.CarDealership.service.UserService;
import ro.itschool.CarDealership.util.Constants;


import java.util.Set;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RoleRepository roleRepository;

    private final UserService userService;

    @GetMapping(value = "/register")
    public String registerForm(Model model) {
        MyUser user = new MyUser();
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        model.addAttribute("user", user);
        return Constants.REGISTER;
    }

    @PostMapping(value = "/register")
    public String registerUser(@ModelAttribute("user") @RequestBody MyUser user) {
        if (user.getPassword().equals(user.getPasswordConfirm())) {
            user.setRoles(Set.of(roleRepository.findByName(Constants.ROLE_USER)));
            userService.saveUser(user);
            return "register-success";
        } else {
            return Constants.REGISTER;
        }
    }

}


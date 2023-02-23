package ro.itschool.CarDealership.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.itschool.CarDealership.util.Constants;

@Controller
public class LogoutController {

    @RequestMapping(value = {"/logout"})
    public String logout(){
        return Constants.LOGIN;
    }
}

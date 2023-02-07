package ro.itschool.CarDealership.service.email.impl;

import org.springframework.stereotype.Service;
import ro.itschool.CarDealership.entity.MyUser;
import ro.itschool.CarDealership.service.email.EmailBodyService;

@Service
public class EmailBodyServiceImpl implements EmailBodyService {

    @Override
    public String emailBody(MyUser myUser) {
        return "Hello," +
                "In order to activate your account please access the following link:\n" +
                "http://localhost:8080/activation/" + myUser.getRandomToken();
    }
}
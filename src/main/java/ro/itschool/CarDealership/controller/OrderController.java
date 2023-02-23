package ro.itschool.CarDealership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.itschool.CarDealership.entity.Order;
import ro.itschool.CarDealership.entity.MyUser;
import ro.itschool.CarDealership.repository.OrderRepository;
import ro.itschool.CarDealership.repository.UserRepository;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/customer-order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/get-all/{userId}")
    public List<Order> getAllOrders(@PathVariable Long userId) throws UserPrincipalNotFoundException {
        Optional<MyUser> optionatUser = userRepository.findById(userId);
        if(optionatUser.isPresent())
            return optionatUser.get().getOrders();
        else
            throw new UserPrincipalNotFoundException("User not found");
    }
}

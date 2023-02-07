package ro.itschool.CarDealership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.itschool.CarDealership.entity.MyUser;
import ro.itschool.CarDealership.repository.ProductRepository;
import ro.itschool.CarDealership.repository.WishListRepository;
import ro.itschool.CarDealership.service.UserService;

@RequestMapping(value = "/wish-list")
@Controller
public class WishListController {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @RequestMapping
    public String getWishListForPrincipal(Model model) {
        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        model.addAttribute("products", userByUserName.getShoppingCart().getProducts());

        return "wish-list";
    }
}


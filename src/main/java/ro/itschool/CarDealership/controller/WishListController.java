package ro.itschool.CarDealership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.itschool.CarDealership.entity.MyUser;
import ro.itschool.CarDealership.entity.Product;
import ro.itschool.CarDealership.repository.OrderRepository;
import ro.itschool.CarDealership.repository.ProductRepository;
import ro.itschool.CarDealership.repository.ShoppingCartRepository;
import ro.itschool.CarDealership.service.UserService;
import ro.itschool.CarDealership.service.WishListService;

import java.util.Optional;

@Controller
@RequestMapping(value = "/wish-list")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @RequestMapping(value = "/to-shopping-cart")
    public String convertToShoppingCart() {

        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser user = userService.findUserByUserName(currentPrincipalName);
        //transferam produsele din wishlist in shoppingCart
        shoppingCartRepository.save(wishListService.convertWishListToShoppingCart(user.getWishList()));
        user.getWishList().getProducts().clear();
        userService.updateUser(user);

        return "order-successful";
    }


    @RequestMapping
    public String getWishListForPrincipal(Model model) {
        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        model.addAttribute("products", userByUserName.getWishList().getProducts());

        return "wish-list";
    }

    @RequestMapping(value = "/product/remove/{productId}")
    public String removeProductFromWishList(@PathVariable Integer productId) {
        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        Optional<Product> optionalProduct = productRepository.findById(productId);

        userByUserName.getWishList().getProducts().removeIf(product -> product.getId().equals(productId));
        userService.updateUser(userByUserName);

        return "redirect:/wish-list";
    }

}


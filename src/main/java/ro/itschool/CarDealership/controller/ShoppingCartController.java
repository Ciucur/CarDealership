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
import ro.itschool.CarDealership.repository.ShoppingCartProductQuantityRepository;
import ro.itschool.CarDealership.repository.ShoppingCartRepository;
import ro.itschool.CarDealership.service.ShoppingCartService;
import ro.itschool.CarDealership.service.UserService;
import ro.itschool.CarDealership.service.WishListService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartProductQuantityRepository quantityRepository;

//    @PutMapping(value = "/add/{cartId}")
//    public ResponseEntity addProductToShoppingCart(@PathVariable Integer cartId, @RequestParam Integer productId) {
//
//        Product product = productRepository.findById(productId).orElseThrow();
//        ShoppingCart cart = shoppingCartService.findById(cartId).orElseThrow();
//
//        cart.addProductToShoppingCart(product);
//        shoppingCartService.update(cart);
//
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping(value = "/remove/{cartId}")
//    public ResponseEntity removeProductFromShoppingCart(@PathVariable Integer cartId, @RequestParam Integer productId) {
//
//        Product product = productRepository.findById(productId).orElseThrow();
//        ShoppingCart cart = shoppingCartService.findById(cartId).orElseThrow();
//
//        cart.removeProductFromShoppingCart(product);
//        shoppingCartService.update(cart);
//
//        return ResponseEntity.ok().build();
//    }

    @RequestMapping(value = "/to-order")
    public String convertToOrder() {

        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser user = userService.findUserByUserName(currentPrincipalName);

        List<Product> productsByShoppingCartId = quantityRepository.getProductsByShoppingCartId(user.getId());
        shoppingCartService.findById(user.getId().intValue()).ifPresent(cart -> {
            cart.setProducts(productsByShoppingCartId);
            user.setShoppingCart(cart);
        });

        orderRepository.save(shoppingCartService.convertShoppingCartToOrder(user.getShoppingCart()));
        user.getShoppingCart().getProducts().clear();
        userService.updateUser(user);

        return "order-successful";
    }

    @RequestMapping
    public String getShoppingCartForPrincipal(Model model) {
        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        List<Product> productsByShoppingCartId = quantityRepository.getProductsByShoppingCartId(userByUserName.getId());


        model.addAttribute("products", productsByShoppingCartId);

        return "shopping-cart";
    }

    @RequestMapping(value = "/product/remove/{productId}")
    public String removeProductFromShoppingCart(@PathVariable Integer productId) {
        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);


        quantityRepository.getProductsByShoppingCartId(userByUserName.getId()).stream()
                .filter(p -> p.getId().equals(productId))
                .peek(p -> {
                    Optional<Product> byId = productRepository.findById(p.getId());
                    byId.ifPresent(pr -> {
                        pr.setQuantity(pr.getQuantity() + p.getQuantity());
                        productRepository.save(byId.get());
                    });
                })
                .findFirst();
        quantityRepository.deleteByShoppingCartIdAndProductId(userByUserName.getId().intValue(), productId);

//        userService.updateUser(userByUserName);

        return "redirect:/shopping-cart";
    }

}



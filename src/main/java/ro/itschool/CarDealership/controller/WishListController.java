package ro.itschool.CarDealership.controller;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.itschool.CarDealership.entity.MyUser;
import ro.itschool.CarDealership.entity.Product;
import ro.itschool.CarDealership.entity.ShoppingCart;
import ro.itschool.CarDealership.entity.ShoppingCartProductQuantity;
import ro.itschool.CarDealership.repository.*;
import ro.itschool.CarDealership.service.UserService;
import ro.itschool.CarDealership.service.WishListService;
import ro.itschool.CarDealership.util.Constants;

import java.util.List;
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

    @Autowired
    private ShoppingCartProductQuantityRepository quantityRepository;

    @Autowired
    private WishListProductQuantityRepository wishListProductQuantityRepository;


    @RequestMapping(value = "/to-shopping-cart/{productId}")
    public String convertToShoppingCart(@PathVariable Integer productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();
        //aducem userul din db pe baza username-ului
          MyUser user = userService.findUserByUserName(currentPrincipalName);
        //adaugam produsele din wishlist in shoppincart
        //shoppingCartId, productId, quantity
        ShoppingCartProductQuantity shoppingCartProductQuantity = new ShoppingCartProductQuantity();
        shoppingCartProductQuantity.setProductId(optionalProduct.get().getId());
        shoppingCartProductQuantity.setShoppingCartId(user.getId().intValue());
        shoppingCartProductQuantity.setQuantity(optionalProduct.get().getQuantity());

        quantityRepository.save(shoppingCartProductQuantity);

//        List<Product> productsByWishListId = wishListProductQuantityRepository.getProductsByWishListId(user.getId());
//        wishListService.findById(user.getId().intValue()).ifPresent(wishlist->{
//            wishlist.setProducts(productsByWishListId);
//            user.setWishList(wishlist);
//        });
        return "redirect:/wish-list";
   }

//    @RequestMapping(value = "/to-shopping-cart/{productId}")
//    public String convertWishListToShoppingCart(Model model) {
//
//        //stabilim care e username-ul user-ului autentificat
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = auth.getName();
//
//        //aducem userul din db pe baza username-ului
//        MyUser user = userService.findUserByUserName(currentPrincipalName);
//
//        List<Product> productsByWishListId = wishListProductQuantityRepository.getProductsByWishListId(user.getId());
//        wishListService.findById(user.getId().intValue()).ifPresent(wl -> {
//            wl.setProducts(productsByWishListId);
//            user.setShoppingCart(wl);
//        });
//
//        ShoppingCart shoppingCart = shoppingCartRepository.save(wishListService.convertWishListToShoppingCart(user.getWishList()));
//        user.getWishList().getProducts().clear();
//        wishListProductQuantityRepository.deleteByWishListId(user.getId().intValue());
//        model.addAttribute("shoppingCart", shoppingCart);
//        return "order-successful";
//    }


    @RequestMapping
    public String getWishListForPrincipal(Model model) {
        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        model.addAttribute("products", userByUserName.getWishList().getProducts());

        return Constants.WISH_LIST;
    }

    @RequestMapping(value = "/product/remove/{productId}")
    public String removeProductFromWishList(@PathVariable Integer productId) {
        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        wishListProductQuantityRepository.getProductsByWishListId(userByUserName.getId()).stream()
                .filter(p -> p.getId().equals(productId))
                .forEach(p -> {
                    Optional<Product> byId = productRepository.findById(p.getId());
                    byId.ifPresent(pr -> {
                        pr.setQuantity(pr.getQuantity() + p.getQuantity());
                        productRepository.save(byId.get());
                    });
                });
        wishListProductQuantityRepository.deleteByWishListIdAndProductId(userByUserName.getId().intValue(), productId);
        return "redirect:/wish-list";
    }

}


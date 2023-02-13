package ro.itschool.CarDealership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.itschool.CarDealership.entity.MyUser;
import ro.itschool.CarDealership.entity.Product;
import ro.itschool.CarDealership.entity.ShoppingCartProductQuantity;
import ro.itschool.CarDealership.repository.ProductRepository;
import ro.itschool.CarDealership.repository.ShoppingCartProductQuantityRepository;
import ro.itschool.CarDealership.repository.WishListRepository;
import ro.itschool.CarDealership.service.ShoppingCartService;
import ro.itschool.CarDealership.service.UserService;
import ro.itschool.CarDealership.service.WishListService;
import ro.itschool.CarDealership.util.Constants;

import java.util.Optional;


@Controller
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ShoppingCartProductQuantityRepository quantityRepository;


//    @GetMapping(value = "/all")
//    public List<Car> getAllCars(){return carRepository.findAll();}

    @RequestMapping(value = {"/all"})
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("products", productRepository.findByQuantityGreaterThan(0L));
        return "products";
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        shoppingCartService.deleteProductByIdFromShoppingCart(id);
        productRepository.deleteById(id);
        return Constants.REDIRECT_TO_PRODUCTS;
    }

    @RequestMapping(value = "/add/{id}")
    public String addProductToShoppingCart(@PathVariable Integer id, @ModelAttribute("product") @RequestBody Product frontendProduct) {
        //cautam produsul dupa id
        Optional<Product> optionalProduct = productRepository.findById(id);

        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        Integer quantityToBeOrdered = frontendProduct.getQuantity();

        //in shopping cart-ul userului adus adaugam produsul trimis din frontend
        optionalProduct.ifPresent(product -> {
            //setez pe produs quantity-ul si il adaug in shopping cart
            Product productToBeAddedToShoppingCart = new Product();
            productToBeAddedToShoppingCart.setId(product.getId());
            productToBeAddedToShoppingCart.setPrice(product.getPrice());
            productToBeAddedToShoppingCart.setName(product.getName());
            productToBeAddedToShoppingCart.setQuantity(quantityToBeOrdered);
            userByUserName.getShoppingCart().addProductToShoppingCart(productToBeAddedToShoppingCart);


            product.setQuantity(product.getQuantity() - quantityToBeOrdered);
            quantityRepository.save(new ShoppingCartProductQuantity(userByUserName.getId().intValue(), product.getId(), quantityToBeOrdered));
            productRepository.save(product);
            userService.updateUser(userByUserName);
        });

        return Constants.REDIRECT_TO_PRODUCTS;
    }

    @GetMapping(value = "/add-new")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping(value = "/add-new")
    public String addProduct(@ModelAttribute("product") @RequestBody Product product) {
        productRepository.save(product);
        return Constants.REDIRECT_TO_PRODUCTS;
    }

    @RequestMapping(value = "/addToWishList/{id}")
    public String addProductToWishList(@PathVariable Integer id) {
        //cautam produsul dupa ID
        Optional<Product> optionalProduct = productRepository.findById(id);

        //stabiim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        //in wish list';ul userului adus adaugam produsul trimis din frontend
        optionalProduct.ifPresent(product -> {
            userByUserName.getWishList().addProductToWishList(product);
            userService.updateUser(userByUserName);
        });

        return Constants.REDIRECT_TO_PRODUCTS;
    }
}

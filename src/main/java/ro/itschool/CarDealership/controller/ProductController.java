package ro.itschool.CarDealership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.itschool.CarDealership.entity.MyUser;
import ro.itschool.CarDealership.entity.Product;
import ro.itschool.CarDealership.entity.ShoppingCartProductQuantity;
import ro.itschool.CarDealership.entity.WishListProductQuantity;
import ro.itschool.CarDealership.repository.ProductRepository;
import ro.itschool.CarDealership.repository.ShoppingCartProductQuantityRepository;
import ro.itschool.CarDealership.repository.WishListProductQuantityRepository;
import ro.itschool.CarDealership.service.ShoppingCartService;
import ro.itschool.CarDealership.service.UserService;
import ro.itschool.CarDealership.util.Constants;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartProductQuantityRepository quantityRepository;
    private final WishListProductQuantityRepository wishListProductQuantityRepository;


//    @GetMapping(value = "/all")
//    public List<Car> getAllCars(){return carRepository.findAll();}

    @RequestMapping(value = {"/all"})
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("products", productRepository.findByQuantityGreaterThan(0L));
        return Constants.PRODUCTS;
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
        Optional<Product> desiredProductOptional = productRepository.findById(id);
        if (frontendProduct == null)  {
            throw new RuntimeException("Quantity can't be null");
        }
        Integer quantityToBeOrdered = frontendProduct.getQuantity();

        //stabilim care e username-ul user-ului autentificat
        MyUser loggedUser = getLoggedUser();


        //in shopping cart-ul userului adus adaugam produsul trimis din frontend
        desiredProductOptional.ifPresent(desiredProduct -> {
            //setez pe produs quantity-ul si il adaug in shopping cart
            Product productToBeAddedToShoppingCart = new Product();
            productToBeAddedToShoppingCart.setId(desiredProduct.getId());
            productToBeAddedToShoppingCart.setPrice(desiredProduct.getPrice());
            productToBeAddedToShoppingCart.setName(desiredProduct.getName());
            productToBeAddedToShoppingCart.setQuantity(quantityToBeOrdered);
            loggedUser.getShoppingCart().addProductToShoppingCart(productToBeAddedToShoppingCart);

            //get SHoppingCartProductQuantity from database daca exista
            //daca nu exista, il salvam ca fiind nou
            desiredProduct.setQuantity(desiredProduct.getQuantity() - quantityToBeOrdered);

            Optional<ShoppingCartProductQuantity> cartProductQuantityOptional = quantityRepository.findByShoppingCartIdAndProductId(loggedUser.getId().intValue(), desiredProduct.getId());
            if (cartProductQuantityOptional.isEmpty()) {
                quantityRepository.save(new ShoppingCartProductQuantity(loggedUser.getId().intValue(), desiredProduct.getId(), quantityToBeOrdered));
            } else {
                cartProductQuantityOptional.ifPresent(cartProductQuantity -> {
                    cartProductQuantity.setQuantity(cartProductQuantity.getQuantity() + quantityToBeOrdered);
                    quantityRepository.save(cartProductQuantity);
                });
            }

            productRepository.save(desiredProduct);
            userService.updateUser(loggedUser);
        });

        return Constants.REDIRECT_TO_PRODUCTS;
    }


    @RequestMapping(value = "/addToWishList/{id}")
    public String addProductToWishList(@PathVariable Integer id, @ModelAttribute("product") @RequestBody Product frontendProduct) {
        //cautam produsul dupa ID
        Optional<Product> optionalProduct = productRepository.findById(id);

        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        Integer quantityToBeOrdered = frontendProduct.getQuantity();


        optionalProduct.ifPresent((product -> {
            Product productToBeAddedToWishList = new Product();
            productToBeAddedToWishList.setId(product.getId());
            productToBeAddedToWishList.setPrice(product.getPrice());
            productToBeAddedToWishList.setName(product.getName());
            productToBeAddedToWishList.setQuantity(quantityToBeOrdered);
            userByUserName.getWishList().addProductToWishList(productToBeAddedToWishList);

            product.setQuantity(product.getQuantity() - quantityToBeOrdered);

            Optional<WishListProductQuantity> optionalEntity = wishListProductQuantityRepository.findByWishListIdAndProductId(userByUserName.getId().intValue(), product.getId());
            if (optionalEntity.isEmpty()) {
                wishListProductQuantityRepository.save(new WishListProductQuantity(userByUserName.getId().intValue(), product.getId(), quantityToBeOrdered));
            } else {
                optionalEntity.ifPresent(opt -> {
                    opt.setQuantity(opt.getQuantity() + quantityToBeOrdered);
                    wishListProductQuantityRepository.save(opt);
                });
            }
            productRepository.save(product);
            userService.updateUser(userByUserName);
        }));

        return Constants.REDIRECT_TO_PRODUCTS;
    }


    @GetMapping(value = "/add-new")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        return Constants.ADD_PRODUCT;
    }

    @PostMapping(value = "/add-new")
    public String addProduct(@ModelAttribute("product") @RequestBody Product product) {
        productRepository.save(product);
        return Constants.REDIRECT_TO_PRODUCTS;
    }

    private MyUser getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);
        return userByUserName;
    }
}

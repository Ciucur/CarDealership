package ro.itschool.CarDealership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.itschool.CarDealership.repository.ProductRepository;
import ro.itschool.CarDealership.repository.ShoppingCartRepository;
import ro.itschool.CarDealership.util.Constants;


@Controller
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;



//    @GetMapping(value = "/all")
//    public List<Car> getAllCars(){return carRepository.findAll();}

    @RequestMapping(value = {"/all"})
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("products", productRepository.findByDeletedIsFalse());
        return "products";
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteProduct(@PathVariable Integer id){
            productRepository.deleteById(id);
            return Constants.REDIRECT_TO_PRODUCTS;
    }
}

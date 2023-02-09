package ro.itschool.CarDealership.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.itschool.CarDealership.entity.ShoppingCart;
import ro.itschool.CarDealership.entity.WishList;
import ro.itschool.CarDealership.repository.ShoppingCartRepository;
import ro.itschool.CarDealership.repository.WishListRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final WishListRepository wishListRepository;

    public Optional<ShoppingCart> findById(Integer id) {
        return shoppingCartRepository.findById(id);
    }


    public ShoppingCart convertWishListToShoppingCart(WishList wishList) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.getProducts().addAll(wishList.getProducts());
        shoppingCart.setUser(wishList.getUser());
        return shoppingCart;
    }

    public WishList update(WishList wishList) {
        return wishListRepository.save(wishList);
    }

    public WishList save(WishList wl) {
        return wishListRepository.save(wl);
    }

    public void deleteProductByIdFromWishList(Integer productId) {
        wishListRepository.findAll().stream()
                .filter(wishList -> wishList.getProducts().removeIf(product -> product.getId().equals(productId)))
                .toList();

    }

}

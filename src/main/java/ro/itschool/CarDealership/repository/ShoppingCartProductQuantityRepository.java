package ro.itschool.CarDealership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ro.itschool.CarDealership.entity.Product;
import ro.itschool.CarDealership.entity.ShoppingCartProductQuantity;


import java.util.List;

public interface ShoppingCartProductQuantityRepository extends JpaRepository<ShoppingCartProductQuantity, Long> {

    @Query(value = "SELECT new ro.itschool.CarDealership.entity.Product(p.id, p.name, p.price, s.quantity) from Product p inner join ShoppingCartProductQuantity s " +
            "on p.id = s.productId where shoppingCartId = :id")
    List<Product> getProductsByShoppingCartId(Long id);

    @Modifying
    @Transactional
    void deleteByShoppingCartIdAndProductId(Integer shoppingCartId, Integer productId);

}


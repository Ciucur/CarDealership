package ro.itschool.CarDealership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ro.itschool.CarDealership.entity.Product;
import ro.itschool.CarDealership.entity.WishListProductQuantity;

import java.util.List;
import java.util.Optional;

public interface WishListProductQuantityRepository extends JpaRepository<WishListProductQuantity, Long> {

    @Query(value = "SELECT new ro.itschool.CarDealership.entity.Product(p.id, p.name, p.price, s.quantity) from Product p inner join WishListProductQuantity s " +
            "on p.id = s.productId where wishListId = :id")
    List<Product> getProductsByWishListId(Long id);

    @Modifying
    @Transactional
    void deleteByWishListIdAndProductId(Integer wishListId, Integer productId);

    Optional<WishListProductQuantity> findByWishListIdAndProductId(Integer wishListId, Integer productId);

    @Modifying
    @Transactional
    void deleteByWishListId(Integer wishListId);
}

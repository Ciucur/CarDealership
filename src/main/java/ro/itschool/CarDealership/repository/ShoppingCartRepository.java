package ro.itschool.CarDealership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.CarDealership.entity.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {


}

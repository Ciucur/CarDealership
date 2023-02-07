package ro.itschool.CarDealership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.CarDealership.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByDeletedIsFalse();
}

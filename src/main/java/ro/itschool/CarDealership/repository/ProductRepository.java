package ro.itschool.CarDealership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.CarDealership.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    List<Product> findByQuantityGreaterThan(Long quantity);
}

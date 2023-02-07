package ro.itschool.CarDealership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.CarDealership.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}

package ro.itschool.CarDealership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.CarDealership.entity.WishList;

public interface WishListRepository extends JpaRepository<WishList, Integer> {
}

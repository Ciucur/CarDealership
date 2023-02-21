package ro.itschool.CarDealership.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishListProductQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer wishListId;
    private Integer productId;
    private Integer quantity;

    public WishListProductQuantity(Integer wishListId, Integer productId, Integer quantity) {
        this.wishListId = wishListId;
        this.productId = productId;
        this.quantity = quantity;
    }


}

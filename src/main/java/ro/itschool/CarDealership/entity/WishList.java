package ro.itschool.CarDealership.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer id;

    //un produs poate fi in mai multe wishlist'uri
    //un wishlist poate avea mai multe produse
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();


    @OneToOne(mappedBy = "wishList")
    private MyUser user;

    public void addProductToWishList(Product p) {
        this.products.add(p);
    }

    public void removeProductFromWishList(Product product) {
        this.products.remove(product);
    }
}

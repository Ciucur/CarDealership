package ro.itschool.CarDealership.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //un cart poate avea mai multe produse
    //un produs poate fi in mai multe carturi
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "shoppingCart")
    private MyUser user;

    public void addProductToShoppingCart(Product p) {
        this.products.add(p);
    }

    public void removeProductFromShoppingCart(Product product) {
        this.products.remove(product);
    }

}

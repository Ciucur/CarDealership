package ro.itschool.CarDealership.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;

    private float price;

    private Integer quantity;

    public Product(String name, float price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }


}

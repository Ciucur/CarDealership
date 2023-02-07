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

    private String name;

    private float price;

    private Boolean deleted;

    public Product(String name, float price, Boolean deleted) {
        this.name = name;
        this.price = price;
        this.deleted = deleted;
    }


}

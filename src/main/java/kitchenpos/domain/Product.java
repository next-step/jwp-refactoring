package kitchenpos.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Price price;

    protected Product() {
    }

    private Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }


    public static Product of(String name, Price price) {
        return new Product(name, price);
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Price getPrice() {
        return this.price;
    }

}

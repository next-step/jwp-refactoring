package kitchenpos.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {
    }

    private Product(String name, Integer price) {
        this.name = Name.of(name);
        this.price = Price.of(price);
    }

    public static Product of(String name, Integer price) {
        return new Product(name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public Integer getPrice() {
        return price.getValue().intValue();
    }
}

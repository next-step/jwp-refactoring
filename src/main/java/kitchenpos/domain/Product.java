package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    protected Product() {}

    private Product(String name, BigDecimal price) {
        this.name = name;
        this.price = Price.from(price);
    }

    public static Product of(BigDecimal price, String name) {
        return new Product(name, price);
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}

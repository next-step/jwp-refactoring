package kitchenpos.product.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price = new Price();

    protected Product() {}

    public Product(Long id, Price price, String name) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(Price price, String name) {
        this.price = price;
        this.name = name;
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

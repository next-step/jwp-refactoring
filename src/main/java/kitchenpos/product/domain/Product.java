package kitchenpos.product.domain;

import kitchenpos.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Price price;

    protected Product() {}

    public Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public static Product create(String name, Price price) {
        return new Product(name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

}

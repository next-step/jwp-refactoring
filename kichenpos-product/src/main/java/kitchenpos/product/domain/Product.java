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

    private Product(Long id) {
        this.id = id;
    }

    private Product(String name, Integer price) {
        this.name = Name.of(name);
        this.price = Price.from(price);
    }

    public static Product of(Long id) {
        return new Product(id);
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

    public Price getPrice() {
        return price;
    }

    public Integer getPriceIntValue() {
        return price.getIntValue();
    }

    public BigDecimal getPriceValue() {
        return price.getValue();
    }
}

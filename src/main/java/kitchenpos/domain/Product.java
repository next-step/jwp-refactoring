package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private ProductPrice price;

    protected Product() {
    }

    public Product(Long id, String name, ProductPrice price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public BigDecimal calculatePrice(long quantity) {
        return price.multiply(quantity);
    }

    public Product(String name, ProductPrice price) {
        this(null, name, price);
    }

    public Product(String name, int price) {
        this(null, name, new ProductPrice(price));
    }

    public Product(String name, BigDecimal price) {
        this(null, name, new ProductPrice(price));
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

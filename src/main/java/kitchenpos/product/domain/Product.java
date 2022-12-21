package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private ProductPrice price;

    protected Product() {}

    private Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = ProductPrice.from(price);
    }

    public static Product generate(String name, BigDecimal price) {
        return new Product(null, name, price);
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public boolean hasId(Long id) {
        return Objects.equals(this.id, id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductPrice getPrice() {
        return price;
    }

    public BigDecimal getPriceValue() {
        return price.value();
    }
}

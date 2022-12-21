package kitchenpos.product.domain;

import static kitchenpos.exception.ErrorCode.PRICE_NOT_EXISTS_OR_LESS_THAN_ZERO;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Price;
import kitchenpos.exception.KitchenposException;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = Price.of(price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = Price.of(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return this.price.getPrice();
    }
}

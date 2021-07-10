package kitchenpos.product.domain;

import kitchenpos.common.Message;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        validateName(name);
        validatePrice(price);
        this.name = name;
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException(Message.ERROR_PRODUCT_PRICE_REQUIRED.showText());
        }
        if (price.compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException(Message.ERROR_PRODUCT_PRICE_SHOULD_BE_OVER_THAN_ZERO.showText());
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(Message.ERROR_PRODUCT_NAME_REQUIRED.showText());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
}

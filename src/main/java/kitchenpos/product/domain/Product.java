package kitchenpos.product.domain;

import kitchenpos.common.valueobject.Price;
import kitchenpos.product.domain.exception.InvalidProductNameException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

    private Product(Long id, String name, BigDecimal price) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.price = Price.of(price);
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || Objects.equals(name, "")) {
            throw new InvalidProductNameException();
        }
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(null, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}

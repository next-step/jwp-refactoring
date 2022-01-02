package kitchenpos.menu.domain;

import java.math.*;
import java.util.*;

import javax.persistence.*;

import kitchenpos.common.*;

@Entity
public class Product {
    private static final String PRICE = "금액";
    private static final String NAME = "이름";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    protected Product() {

    }

    public Product(String name, BigDecimal price) {
        validate(name, price);
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price);
    }

    private void validate(String name, BigDecimal price) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new WrongValueException(NAME);
        }

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new WrongValueException(PRICE);
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

}

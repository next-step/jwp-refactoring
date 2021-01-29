package kitchenpos.product.domain;

import kitchenpos.menu.domain.MenuProduct;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void validationCheck() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("잘못된 값을 입력하셨습니다.");
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

package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(nullable = false)
    private BigDecimal price;

    public Product(String name, BigDecimal price) {
        validate(price);
        this.name = name;
        this.price = price;
    }

    protected Product() {
    }

    public BigDecimal getSumPrice(Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
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

    private void validate(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Product의 price는 필수입니다.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product의 price는 0이상이어야합니다.");
        }
    }
}

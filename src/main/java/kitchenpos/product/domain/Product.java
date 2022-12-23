package kitchenpos.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    private static final String INVALID_PRICE = "유요하지 않은 가격입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private BigDecimal price;

    public Product() {

    }

    public Product(String name, BigDecimal price) {
        validatePrice(price);
        this.name = name;
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(INVALID_PRICE);
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

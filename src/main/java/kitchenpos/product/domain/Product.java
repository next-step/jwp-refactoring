package kitchenpos.product.domain;

import kitchenpos.ExceptionMessage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    private static final int MINIMUM_PRICE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {}

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, BigDecimal price) {
        if (Objects.isNull(price) ||
                price.compareTo(new BigDecimal(MINIMUM_PRICE)) < 0) {
            throw new IllegalArgumentException(ExceptionMessage.PRODUCT_PRICE_LOWER_THAN_MINIMUM
                    .getMessage());
        }
        this.name = name;
        this.price = price;
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

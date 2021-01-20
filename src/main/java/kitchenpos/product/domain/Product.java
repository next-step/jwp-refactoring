package kitchenpos.product.domain;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    public static final int ZERO_VALUE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        validateName(name);
        validatePrice(price);
        this.name = name;
        this.price = new Price(price);
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름은 필수 정보입니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < ZERO_VALUE) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public Price calculatePricePerQuantity(Quantity quantity) {
        return price.multiply(quantity.toBigDecimal());
    }
}

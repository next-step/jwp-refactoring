package kitchenpos.domain;

import kitchenpos.domain.exceptions.product.InvalidProductPriceException;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    public Product(final Long id, final String name, final BigDecimal price) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
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

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException("상품의 가격은 반드시 있어야 하며, 0원 이상이어야 합니다.");
        }
    }
}

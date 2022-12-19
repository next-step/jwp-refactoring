package kitchenpos.product.domain;

import kitchenpos.exception.ProductError;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {

    }
    private Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ProductError.INVALID_PRICE);
        }
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public BigDecimal getPrice() {
        return price;
    }
}

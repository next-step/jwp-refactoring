package kitchenpos.product.domain;

import kitchenpos.product.exception.ProductException;
import kitchenpos.product.exception.ProductExceptionType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price {
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    @Column(name = "sprice", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    private Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (value == null || MIN_PRICE.compareTo(value) >= BigDecimal.ZERO.intValue()) {
            throw new ProductException(ProductExceptionType.MIN_PRICE);
        }
    }

    public static Price of(final BigDecimal value) {
        return new Price(value);
    }

    public BigDecimal getValue() {
        return value;
    }

}

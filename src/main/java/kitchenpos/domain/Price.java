package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    private static final String INVALID_PRICE = "Price 는 0 이상의 값을 가집니다.";

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Price() {}

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        validatePrice(price);
        return new Price(price);
    }

    public int compareTo(BigDecimal value) {
        return price.compareTo(value);
    }

    public BigDecimal getValue() {
        return price;
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(INVALID_PRICE);
        }
    }
}

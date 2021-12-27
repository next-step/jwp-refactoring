package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final int ZERO = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        validate(price);

        this.price = price;
    }

    private void validate(BigDecimal price) {
        validateNotNull(price);
        validateNotNegative(price);
    }

    private void validateNotNull(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("가격은 null 일 수 없습니다.");
        }
    }

    private void validateNotNegative(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다.");
        }
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isLessThan(final BigDecimal toCompare) {
        return price.compareTo(toCompare) > ZERO;
    }
}

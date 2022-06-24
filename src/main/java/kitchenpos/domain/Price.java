package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    public static final BigDecimal MIN = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    private static void validatePrice(BigDecimal price) {
        validateNotNull(price);
        validateNotUnderMin(price);
    }

    private static void validateNotNull(BigDecimal price) {
        if(Objects.isNull(price)) {
            throw new IllegalArgumentException("금액을 지정해야 합니다.");
        }
    }

    private static void validateNotUnderMin(BigDecimal price) {
        if (price.compareTo(MIN) < 0) {
            throw new IllegalArgumentException("금액은 " + MIN + "원 이하가 될 수 없습니다.");
        }
    }

    public BigDecimal value() {
        return price;
    }
}


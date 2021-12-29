package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.MenuErrorCode;
import kitchenpos.exception.InvalidParameterException;

@Embeddable
public class Price {

    private static final BigDecimal MIN = BigDecimal.ZERO;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        validateMinPrice(price);
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public BigDecimal value() {
        return price;
    }

    public boolean greaterThanOf(BigDecimal price) {
        return this.price.compareTo(price) > 0;
    }

    private void validateMinPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MIN) < 0) {
            throw new InvalidParameterException(
                MenuErrorCode.MENU_PRICE_MIN_UNDER_EXCEPTION);
        }
    }
}

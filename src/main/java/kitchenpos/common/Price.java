package kitchenpos.common;

import static kitchenpos.exception.KitchenposExceptionMessage.PRICE_CANNOT_LOWER_THAN_MIN;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.KitchenposException;

@Embeddable
public class Price {

    private static final BigDecimal MIN = BigDecimal.ZERO;
    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column(name = "price", precision = 19, scale = 2)
    private BigDecimal value;

    protected Price() {
        // empty
    }

    private Price(final BigDecimal value) {
        this.value = value;
    }

    public static Price of(final BigDecimal price) {
        checkPriceGreaterThanMin(price);
        return new Price(price);
    }

    private static void checkPriceGreaterThanMin(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MIN) < 0) {
            throw new KitchenposException(PRICE_CANNOT_LOWER_THAN_MIN);
        }
    }

    public Price add(final Price price) {
        return Price.of(this.value.add(price.value));
    }

    public Price multiply(final long times) {
        return Price.of(this.value.multiply(BigDecimal.valueOf(times)));
    }

    public boolean isBiggerThan(final Price price) {
        return this.value.compareTo(price.value) > 0;
    }

    public BigDecimal value() {
        return this.value;
    }
}

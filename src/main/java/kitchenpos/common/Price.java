package kitchenpos.common;

import static kitchenpos.exception.KitchenposExceptionMessage.PRICE_CANNOT_LOWER_THAN_MIN;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.KitchenposException;

@Embeddable
public class Price {

    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;
    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    protected Price() {
        // empty
    }

    private Price(final BigDecimal price) {
        this.price = price;
    }

    public static Price of(final BigDecimal price) {
        checkPriceGreaterThanMin(price);
        return new Price(price);
    }

    public Price add(final Price price) {
        return Price.of(this.price.add(price.price));
    }

    public Price multiply(final long times) {
        return Price.of(this.price.multiply(BigDecimal.valueOf(times)));
    }

    private static void checkPriceGreaterThanMin(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MIN_PRICE) < 0) {
            throw new KitchenposException(PRICE_CANNOT_LOWER_THAN_MIN);
        }
    }

    public boolean isBiggerThan(final Price price) {
        return this.price.compareTo(price.price) > 0;
    }

    public BigDecimal getValue() {
        return this.price;
    }
}

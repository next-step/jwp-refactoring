package kitchenpos.product.domain;

import static java.lang.String.valueOf;
import static kitchenpos.common.exception.Message.AMOUNT_IS_NOT_LESS_THAN_ZERO;
import static kitchenpos.common.exception.Message.AMOUNT_PRICE_IS_NOT_EMPTY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Amount implements Serializable {

    @Embedded
    public static final Amount ZERO = new Amount(BigDecimal.ZERO);

    @Column(nullable = false)
    private BigDecimal price;

    public Amount getZERO() {
        return ZERO;
    }

    public static <T extends Number> Amount of(T price) {
        validPriceIsNotEmpty(price);
        validPriceIsNotLessThanZero(price);
        return new Amount(new BigDecimal(valueOf(price)));
    }

    private static <T extends Number> void validPriceIsNotEmpty(T price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(AMOUNT_PRICE_IS_NOT_EMPTY.getMessage());
        }
    }

    private static <T extends Number> void validPriceIsNotLessThanZero(T price) {
        if (isLessThanZero(price)) {
            throw new IllegalArgumentException(AMOUNT_IS_NOT_LESS_THAN_ZERO.getMessage());
        }
    }

    public static <T extends Number> boolean isLessThanZero(T price) {
        return valueOf(price).compareTo("0") <= 0;
    }

    private Amount(BigDecimal price) {
        this.price = price;
    }

    protected Amount() {
    }

    public Amount add(Amount amount) {
        return new Amount(this.price.add(amount.price));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Amount amount = (Amount) o;
        return Objects.equals(price, amount.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public BigDecimal getPrice() {
        return this.price;
    }
}

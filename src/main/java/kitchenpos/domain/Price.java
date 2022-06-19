package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    public static final String INVALID_PRICE = "Price는 0원 보다 작은 값일 수 없습니다. (input = %s)";
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {}

    public Price(BigDecimal price) {
        this.value = price;
    }

    public static Price from(BigDecimal price) {
        validatePrice(price);
        return new Price(price);
    }

    public BigDecimal getValue() {
        return this.value;
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(MIN_PRICE) <= 0) {
            throw new IllegalArgumentException(String.format(INVALID_PRICE, price));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(getValue(), price.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

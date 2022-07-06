package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal value;

    protected Price() {
        this(BigDecimal.ZERO);
    }

    protected Price(BigDecimal price) {
        validatePrice(price);
        this.value = price;
    }

    public static Price create() {
        return new Price();
    }

    public static Price from(long price) {
        return new Price(new BigDecimal(price));
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 NULL이 될 수 없으며, 0이상이어야 합니다.");
        }
    }

    public BigDecimal value() {
        return value;
    }

    public boolean isGreaterThan(Price price) {
        return value.compareTo(price.value) > 0;
    }

    public Price multiply(long number) {
        return new Price(value.multiply(new BigDecimal(number)));
    }

    public Price add(Price price) {
        return new Price(value.add(price.value));
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
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

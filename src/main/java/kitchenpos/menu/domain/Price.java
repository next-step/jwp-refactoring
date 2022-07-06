package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    public static final int ZERO = 0;
    private static final long BASE_QUANTITY = 1L;
    private static final int DECIMAL = 2;

    @Column(name = "price")
    private BigDecimal value = BigDecimal.ZERO;

    public Price() {
    }

    public Price(BigDecimal value) {
        validatePrice(value);
        this.value = value.setScale(DECIMAL);
    }

    private void validatePrice(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    public BigDecimal value() {
        return value;
    }

    public boolean isExpensive(Price price) {
        return price.value.compareTo(value) < ZERO;
    }

    public Price add(Price price) {
        return new Price(value.add(price.value));
    }

    public Price multiply(long quantity) {
        if (quantity == BASE_QUANTITY) {
            return this;
        }
        return new Price(value.multiply(BigDecimal.valueOf(quantity)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

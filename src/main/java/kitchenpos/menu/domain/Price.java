package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final String EXCEPTION_MESSAGE_PRICE_IS_NOT_NEGATIVE = "가격은 음수일 수 없습니다.";
    private static final int ZERO = 0;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public BigDecimal value() {
        return price;
    }

    private void validatePrice(BigDecimal price) {
        if (null == price || price.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_PRICE_IS_NOT_NEGATIVE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public boolean isLarger(BigDecimal comparePrice) {
        return this.price.compareTo(comparePrice) > ZERO;
    }
}

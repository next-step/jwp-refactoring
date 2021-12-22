package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public class Price {

    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price fromInteger(Integer price) {
        try {
            BigDecimal bigDecimal = BigDecimal.valueOf(price);
            return new Price(bigDecimal);
        } catch (NullPointerException e) {
            throw new InvalidArgumentException("가격은 필수입니다.");
        }
    }

    public static Price valueOf(BigDecimal price) {
        return new Price(price);
    }

    /**
     * 가격은 0원 이상이다. 가격은 필수이다.
     */
    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new InvalidArgumentException("가격은 필수입니다.");
        }
        if (price.compareTo(MIN_PRICE) < 0) {
            throw new InvalidArgumentException(String.format("가격은 %s 이상이어야 합니다.", MIN_PRICE));
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Price multiply(Long multiply) {
        return Price.valueOf(this.price.multiply(BigDecimal.valueOf(multiply)));
    }

    public boolean isSmallerThan(Price other) {
        return this.price.compareTo(other.price) == -1;
    }

    public boolean isGreaterThan(Price other) {
        return price.compareTo(other.price) == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price other = (Price) o;
        return Objects.equals(price, other.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}

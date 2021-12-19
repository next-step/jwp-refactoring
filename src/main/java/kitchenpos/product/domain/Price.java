package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    private static final Integer MIN_PRICE = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(Integer price) {
        validate(price);
        this.price = BigDecimal.valueOf(price);
    }

    public static Price valueOf(Integer price) {
        return new Price(price);
    }

    /**
     * 가격은 0원 이상이다.
     *
     * @param price
     */
    private void validate(Integer price) {
        if (price == null) {
            throw new IllegalArgumentException();
        }
        if (price < MIN_PRICE) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal get() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o.getClass().equals(BigDecimal.class)) {
            return price.equals(o);
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

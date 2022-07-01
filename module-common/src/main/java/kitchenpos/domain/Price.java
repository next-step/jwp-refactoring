package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price implements Comparable<Price> {
    public static final BigDecimal MIN = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    private static void validatePrice(BigDecimal price) {
        validateNotNull(price);
        validateNotUnderMin(price);
    }

    private static void validateNotNull(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("금액을 지정해야 합니다.");
        }
    }

    private static void validateNotUnderMin(BigDecimal price) {
        if (price.compareTo(MIN) < 0) {
            throw new IllegalArgumentException("금액은 " + MIN + "원 미만이 될 수 없습니다.");
        }
    }

    public BigDecimal value() {
        return price;
    }

    public Price multiply(Quantity quantity) {
        return Price.from(price.multiply(BigDecimal.valueOf(quantity.value())));
    }

    public void add(Price price) {
        this.price = this.price.add(price.value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    @Override
    public int compareTo(Price other) {
        return price.compareTo(other.price);
    }
}

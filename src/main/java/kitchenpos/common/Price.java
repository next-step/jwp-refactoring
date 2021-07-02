package kitchenpos.common;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    public static final int PRICE_ZERO = 0;
    private BigDecimal price;

    protected Price() {}

    private Price(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price of(int price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price of(long price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public Price add(Price other) {
        return new Price(this.price.add(other.price));
    }

    public Price subtract(Price other) {
        return new Price(this.price.subtract(other.getPrice()));
    }

    public Price multiply(long count) {
        return new Price(this.price.multiply(BigDecimal.valueOf(count)));
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    private void validate(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < PRICE_ZERO) {
            throw new IllegalArgumentException("가격은 0 원 이상이어야 합니다.");
        }
    }

    public int intValue() {
        return this.price.intValue();
    }

    public boolean isBiggerThan(Price other) {
        return price.intValue() > other.price.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}

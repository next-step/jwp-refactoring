package kitchenpos.domain;

import kitchenpos.exception.InvalidPriceException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price implements Comparable<Price> {
    private BigDecimal price;

    protected Price() {
    }

    public Price(int price) {
        this(new BigDecimal(price));
    }

    public Price(long price) {
        this(new BigDecimal(price));
    }

    public Price(BigDecimal price) {
        validate(price);

        this.price = price;
    }

    public Price plus(Price target) {
        return new Price(this.price.add(target.price));
    }

    public Price multiply(Price price) {
        return new Price(this.price.multiply(price.price));
    }

    public BigDecimal toBigDecimal() {
        return price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
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

    @Override
    public int compareTo(Price o) {
        return this.price.compareTo(o.price);
    }
}

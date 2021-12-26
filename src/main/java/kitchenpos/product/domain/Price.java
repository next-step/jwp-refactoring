package kitchenpos.product.domain;

import kitchenpos.product.exception.NegativePriceException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    public static final int MIN = 0;

    private BigDecimal price;

    public Price(BigDecimal price) {
        validateNegativePrice(price);
        this.price = price;
    }

    public Price() {

    }

    public static Price from(int price) {
        return new Price(new BigDecimal(price));
    }

    public int compareTo(Price price) {
        return this.price.compareTo(price.toBigDecimal());
    }

    private void validateNegativePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MIN) {
            throw new NegativePriceException();
        }
    }

    public Price add(Price price) {
        return new Price(this.price.add(price.toBigDecimal()));
    }

    public BigDecimal toBigDecimal() {
        return price;
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

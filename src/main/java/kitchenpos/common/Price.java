package kitchenpos.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static Price ZERO = new Price(0);

    private BigDecimal price;

    protected Price() {
    }

    public Price(long price) {
        this(BigDecimal.valueOf(price));
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격정보가 없거나 0원미만이면 안됩니다.");
        }
        this.price = price;
    }

    public static Price sum(List<Price> totalProductsPrice) {
        return totalProductsPrice.stream()
            .reduce(ZERO, Price::add);
    }

    public boolean isGreaterThan(Price otherPrice) {
        return price.compareTo(otherPrice.price) > 0;
    }

    public Price multiply(BigDecimal amount) {
        return new Price(price.multiply(amount));
    }

    public Price add(Price otherPrice) {
        return new Price(price.add(otherPrice.price));
    }

    public BigDecimal price() {
        return price;
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
}

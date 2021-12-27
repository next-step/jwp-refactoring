package kitchenpos.common.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.PriceNotAcceptableException;

@Embeddable
public class Price implements Comparable<Price> {

    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price valueOf(BigDecimal price) {
        validatePrice(price);
        return new Price(price);
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceNotAcceptableException();
        }
    }

    public static Price sumPrices(List<Price> prices) {
        Price sum = prices.stream()
            .reduce(Price.valueOf(BigDecimal.ZERO), (subSum, price) -> subSum.add(price));
        return sum;
    }

    public Price multiply(Quantity quantity) {
        return Price.valueOf(price.multiply(BigDecimal.valueOf(quantity.getQuantity())));
    }

    private Price add(Price addPrice) {
        return Price.valueOf(price.add(addPrice.getPrice()));
    }

    public boolean isBiggerThan(Price value) {
        return compareTo(value) > 0;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public int compareTo(Price o) {
        return price.compareTo(o.getPrice());
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
        return Objects.equals(getPrice(), price.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrice());
    }
}

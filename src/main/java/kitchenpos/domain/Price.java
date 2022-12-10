package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.constant.ErrorCode;

@Embeddable
public class Price implements Comparable<Price> {

    public static final Price ZERO_PRICE = Price.from(BigDecimal.ZERO);

    @Column(nullable = false, columnDefinition = "DECIMAL(19, 2)")
    private BigDecimal price;

    protected Price() {}

    private Price(BigDecimal price) {
        validatePriceIsNull(price);
        validatePriceIsSmallerThanZero(price);
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    private void validatePriceIsNull(BigDecimal price) {
        if(price == null) {
            throw new IllegalArgumentException(ErrorCode.가격은_비어있을_수_없음.getErrorMessage());
        }
    }

    private void validatePriceIsSmallerThanZero(BigDecimal price) {
        if(price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorCode.가격은_0보다_작을_수_없음.getErrorMessage());
        }
    }

    public Price multiply(Quantity quantity) {
        return new Price(price.multiply(quantity.toBigDecimal()));
    }

    public Price add(Price addPrice) {
        return new Price(this.price.add(addPrice.price));
    }

    public BigDecimal value() {
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

    @Override
    public int compareTo(Price comparePrice) {
        return this.price.compareTo(comparePrice.price);
    }
}

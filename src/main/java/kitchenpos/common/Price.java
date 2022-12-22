package kitchenpos.common;

import kitchenpos.ExceptionMessage;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    private static final BigDecimal MINIMUM_PRICE = BigDecimal.ZERO;

    BigDecimal price;

    protected Price() {
        this.price = MINIMUM_PRICE;
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MINIMUM_PRICE) < 0) {
            throw new IllegalArgumentException(ExceptionMessage.PRODUCT_PRICE_LOWER_THAN_MINIMUM.getMessage());
        }
        this.price = price;
    }

    public Price multiplyQuantity(Quantity quantity) {
        BigDecimal newPrice = this.price.multiply(new BigDecimal(quantity.getValue()));
        return new Price(newPrice);
    }

    public Price add(Price price) {
        BigDecimal newPrice = this.price.add(price.getValue());
        return new Price(newPrice);
    }

    public boolean lessOrEqualThan(Price productTotalSum) {
        BigDecimal thisPrice = this.price;
        BigDecimal comparePrice = productTotalSum.getValue();
        return thisPrice.compareTo(comparePrice) <= 0;
    }

    public BigDecimal getValue() {
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

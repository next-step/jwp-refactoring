package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.constants.ErrorMessages;

@Embeddable
public class Price implements Comparable<Price> {

    @Column(nullable = false)
    private BigDecimal price;

    public Price() {}

    public Price(BigDecimal price) {
        validatePriceNotNegative(price);
        this.price = price;
    }

    public Price(int i) {
        this(new BigDecimal(i));
    }

    private void validatePriceNotNegative(BigDecimal val) {
        if (val.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.MENU_PRICE_CANNOT_BE_LESS_THAN_ZERO);
        }
    }

    public void add(BigDecimal val) {
        this.price = this.price.add(val);
    }

    public int compareTo(BigDecimal val) {
        return this.price.compareTo(val);
    }

    @Override
    public int compareTo(Price o) {
        return this.price.compareTo(o.price);
    }

    public BigDecimal getPrice() {
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

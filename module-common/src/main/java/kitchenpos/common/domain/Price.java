package kitchenpos.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.InvalidPriceException;

@Embeddable
public class Price {
    private static final Long MIN_PRICE = 0L;

    @Column(nullable = false)
    private Long price;

    protected Price() {
    }

    private Price(Long price) {
        validatePrice(price);
        this.price = price;
    }

    public static Price of(Long price) {
        return new Price(price);
    }

    public static Price zero() {
        return new Price(0L);
    }

    public Price multiply(Long quantity) {
        return new Price(this.price * quantity);
    }

    public Price sum(Price other) {
        return new Price(this.price + other.price);
    }

    public Boolean isMoreThan(Price totalPrice) {
        return this.price > totalPrice.price;
    }

    public Long value() {
        return this.price;
    }

    private void validatePrice(Long price) {
        if (price == null || price < MIN_PRICE) {
            throw new InvalidPriceException();
        }
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

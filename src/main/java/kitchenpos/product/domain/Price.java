package kitchenpos.product.domain;

import kitchenpos.product.exception.NegativePriceException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    public static final String MESSAGE_NEGATIVE_PRICE = "가격이 0보다 작거나 같을 수 없습니다";

    public static final int MIN_PRICE = 0;

    public static Price Zero = new Price(BigDecimal.ZERO);

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
        return this.price.compareTo(price.getPrice());
    }

    private void validateNegativePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MIN_PRICE) {
            throw new NegativePriceException(MESSAGE_NEGATIVE_PRICE);
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

    public BigDecimal multiply(BigDecimal price) {
        return this.price.multiply(price);
    }

    public Price add(Price price) {
        return new Price(this.price.add(price.getPrice()));
    }

    public BigDecimal getPrice() {
        return price;
    }
}

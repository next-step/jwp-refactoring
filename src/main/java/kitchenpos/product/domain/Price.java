package kitchenpos.product.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    private BigDecimal price;

    public Price() {
    }

    public Price(BigDecimal price) {
        this.price = price;

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 필수이고 0보다 작은값이 될수 없습니다.");
        }
    }

    public Price multiply(BigDecimal quantity) {
        return new Price(price.multiply(quantity));
    }

    public BigDecimal getPrice() {
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

    public static Price add(Price totalPrice, Price addPrice) {
        return new Price(totalPrice.price.add(addPrice.price));
    }

    public int compareTo(Price totalPrice) {
        return this.price.compareTo(totalPrice.price);
    }
}

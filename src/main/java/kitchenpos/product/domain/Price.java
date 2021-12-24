package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    @Column(name = "price")
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price of(Integer value) {
        return new Price(BigDecimal.valueOf(value));
    }

    public void validate() {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 없거나 0보다 작습니다");
        }
    }

    public BigDecimal getValue() {
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
    public String toString() {
        return "Price{" +
            "price=" + price +
            '}';
    }
}

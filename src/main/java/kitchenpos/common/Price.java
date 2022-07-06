package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(name = "price", nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    public Price() {
    }

    public Price(int price) {
        this(BigDecimal.valueOf(price));
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 미만으로 설정할 수 없습니다.");
        }
        this.price = price;
    }

    public BigDecimal value() {
        return price;
    }

    public int compareTo(Price price) {
        return this.price.compareTo(price.price);
    }

    public Price multiply(Quantity quantity) {
        return new Price(price.multiply(quantity.toBigDecimal()));
    }

    public Price plus(Price price) {
        return new Price(this.price.add(price.price));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    public boolean moreExpensiveThan(Price price) {
        return this.price.compareTo(price.price) > 0;
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

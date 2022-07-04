package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    public static BigDecimal MIN_PRICE = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal element;

    protected Price() {
    }

    public Price(int price) {
        this(BigDecimal.valueOf(price));
    }

    public Price(BigDecimal price) {
        validate(price);
        this.element = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MIN_PRICE) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 0보다 작을 수 없습니다.");
        }
    }

    public BigDecimal get() {
        return element;
    }

    public boolean biggerThan(Price price) {
        return this.element.compareTo(price.get()) > 0;
    }

    public void sum(Price price) {
        this.element = this.element.add(price.get());
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

        return Objects.equals(element, price.element);
    }

    @Override
    public int hashCode() {
        return element != null ? element.hashCode() : 0;
    }
}

package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Price implements Comparable<Price> {

    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;
    private static final int COMPARISON_EQUAL_TO = 0;

    @Transient
    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    public Price(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("가격이 올바르지 않으면 상품을 등록할 수 없습니다.");
        }

        if (isLessThanLowerBound(price)) {
            throw new IllegalArgumentException("가격은 " + MIN_PRICE + "원 이상이어야 합니다.");
        }
    }

    private boolean isLessThanLowerBound(final BigDecimal price) {
        return price.compareTo(MIN_PRICE) < COMPARISON_EQUAL_TO;
    }

    public Price add(final Price o) {
        final BigDecimal augend = o.price;
        final BigDecimal addedValue = price.add(augend);
        return new Price(addedValue);
    }

    public Price multiply(final Quantity quantity) {
        final BigDecimal qty = quantity.asBigDecimal();
        final BigDecimal multipliedValue = this.price.multiply(qty);
        return new Price(multipliedValue);
    }

    public BigDecimal asBigDecimal() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    @Override
    public int compareTo(final Price o) {
        final BigDecimal val = o.price;
        return price.compareTo(val);
    }
}

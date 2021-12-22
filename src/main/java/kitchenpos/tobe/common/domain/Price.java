package kitchenpos.tobe.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final BigDecimal LOWER_BOUND = BigDecimal.ZERO;
    private static final int ZERO = 0;

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
            throw new IllegalArgumentException("상품의 가격이 올바르지 않으면 상품을 등록할 수 없습니다.");
        }

        if (isLessThanLowerBound(price)) {
            throw new IllegalArgumentException("상품의 가격은 " + LOWER_BOUND + "원 이상이어야 합니다.");
        }
    }

    private boolean isLessThanLowerBound(final BigDecimal price) {
        return price.compareTo(LOWER_BOUND) < ZERO;
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
}

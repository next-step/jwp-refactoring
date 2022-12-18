package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    private static final int ZERO = 0;
    private static final String REQUIRED_PRICE = "가격은 필수 값 입니다.";
    private static final String INVALID_PRICE = "가격은 0원 이하 일 수 없습니다.";

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {}

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(REQUIRED_PRICE);
        }
        if (value.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException(INVALID_PRICE);
        }
    }

    public BigDecimal value() {
        return value;
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
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

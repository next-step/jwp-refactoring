package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final String ERROR_MESSAGE_INVALID_PRICE = "가격은 0 원 이상이어야 합니다.";

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_PRICE);
        }
    }

    public Price add(BigDecimal price) {
        return new Price(this.price.add(price));
    }

    public boolean isMoreExpensive(Price price) {
        return this.price.compareTo(price.getPrice()) > 0;
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
        Price that = (Price) o;
        return Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrice());
    }
}

package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.domain.Quantity;

@Embeddable
public class Price {

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validationPrice(price);
        this.price = price;
    }

    public Price(Long price) {
        BigDecimal value = BigDecimal.valueOf(price);
        validationPrice(value);
        this.price = value;
    }

    private void validationPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return price;
    }

    public Price multiply(Quantity quantity) {
        return new Price(price.multiply(BigDecimal.valueOf(quantity.getValue())));
    }

    public Price add(Price price) {
        return new Price(this.price.add(price.getValue()));
    }

    public boolean greaterThan(Price source) {
        return price.compareTo(source.getValue()) > 0;
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

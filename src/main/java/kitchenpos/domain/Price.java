package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        verifyAvailable(price);
        this.price = price;
    }

    private void verifyAvailable(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 null 이거나 음수일 수 없습니다.");
        }
    }

    public BigDecimal value() {
        return price;
    }

    public Price multiply(Price operand) {
        return new Price(price.multiply(operand.value()));
    }

    public Price add(Price operand) { return new Price(price.add(operand.value())); }

    public int compareTo(Price operand) {
        return price.compareTo(operand.value());
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
}

package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductPrice {
    private final BigDecimal value;

    public ProductPrice(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException("상품의 가격은 반드시 있어야 하며, 0원 이상이어야 합니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductPrice that = (ProductPrice) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ProductPrice{" +
                "value=" + value +
                '}';
    }
}

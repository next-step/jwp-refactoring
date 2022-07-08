package kitchenpos.domain.product;

import kitchenpos.exception.product.PriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    protected Price() {
    }

    public Price(BigDecimal price) {
        priceValid(price);
        this.price = price;
    }

    private void priceValid(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceException(PriceException.INVALID_PRICE_MSG);
        }
    }

    public BigDecimal getPrice() {
        return price;
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

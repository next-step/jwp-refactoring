package kitchenpos.product.domain;

import static java.math.BigDecimal.*;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.menu.domain.Quantity;
import kitchenpos.product.exception.InvalidPriceException;

@Embeddable
public class Price {
    private BigDecimal price;

    public Price() {
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(ZERO) < 0) {
            throw new InvalidPriceException();
        }
        this.price = price;
    }

    public Price getTotalPrice(Quantity quantity) {
        return new Price(this.price.multiply(BigDecimal.valueOf(quantity.value())));
    }

    public BigDecimal value() {
        return price;
    }

    public Price add(Price price) {
        return new Price(this.value().add(price.value()));
    }

    public boolean lessThan(Price price) {
        return this.price.compareTo(price.value()) < 0;
    }
}

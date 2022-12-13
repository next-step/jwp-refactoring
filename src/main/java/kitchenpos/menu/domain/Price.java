package kitchenpos.menu.domain;

import kitchenpos.exception.MenuError;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {

    }

    private Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(MenuError.INVALID_PRICE);
        }
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int compareTo(BigDecimal price) {
        return this.price.compareTo(price);
    }
}

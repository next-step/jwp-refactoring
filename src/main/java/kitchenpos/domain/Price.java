package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {}

    private Price(BigDecimal price) {
        validCheckPrice(price);
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    private void validCheckPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}

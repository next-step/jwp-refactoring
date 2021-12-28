package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Embeddable
public class Price {
    @Transient
    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column
    private BigDecimal price = BigDecimal.ZERO;

    protected Price() {}

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public boolean isMinus() {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isGreaterThan(BigDecimal price) {
        return this.price.compareTo(price) > 0;
    }

    public BigDecimal get() {
        return price;
    }

    public BigDecimal multiply(BigDecimal quantity) {
        return price.multiply(quantity);
    }
}

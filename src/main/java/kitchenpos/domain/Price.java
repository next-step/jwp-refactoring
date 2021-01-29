package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {

    }

    public Price(BigDecimal Value) {
        this.value =Value;
    }

    public BigDecimal getValue() {
        return value;
    }
}

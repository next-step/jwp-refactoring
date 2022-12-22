package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    private BigDecimal price;

    protected Price() {}

    public Price(BigDecimal price) {
        this.price = price;
    }

    public static Price create(BigDecimal price) {
        Price created = new Price(price);
        if (created.isNull() || created.isNegative()) {
            throw new IllegalArgumentException("가격의 금액은 NULL이거나 음수여서는 안됩니다.");
        }
        return created;
    }

    public BigDecimal value() {
        return price;
    }

    public boolean isNull() {
        return Objects.isNull(price);
    }

    public boolean isNegative() {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public void add(BigDecimal add) {
        price = price.add(add);
    }

    public boolean isGather(Price sum) {
        return price.compareTo(sum.value()) > 0;
    }
}

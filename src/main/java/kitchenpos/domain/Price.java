package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price {
    private BigDecimal price;

    protected Price() {
    }

    private Price(long price) {
        this.price = BigDecimal.valueOf(price);
    }

    public static Price of(long price) {
        validatePrice(price);
        return new Price(price);
    }

    private static void validatePrice(long price) {
        if (price <= 0) {
            throw new IllegalArgumentException("가격을 0원보다 커야합니다");
        }
    }

    public long longValue() {
        return price.longValue();
    }
}

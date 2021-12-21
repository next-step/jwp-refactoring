package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

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

package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;
import org.springframework.util.ObjectUtils;

@Embeddable
public class Price {
    private static final int COMPARE_EQUAL_NUMBER = 0;
    private static final int COMPARE_BIG_NUMBER = 1;

    private BigDecimal price;

    protected Price() {

    }

    private Price(BigDecimal price) {
        validPrice(price);
        this.price = price;
    }

    private void validPrice(BigDecimal price) {
        if (ObjectUtils.isEmpty(price) || isZeroOver(price)) {
            throw new IllegalArgumentException("가격은 0원 초과이어야 합니다.");
        }
    }

    private boolean isZeroOver(BigDecimal price) {
        return BigDecimal.ZERO.compareTo(price) >= COMPARE_EQUAL_NUMBER;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public static Price of(int price) {
        return new Price(BigDecimal.valueOf(price));
    }


    public BigDecimal value() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public boolean isBigThen(Price target) {
        return this.value().compareTo(target.value()) > COMPARE_EQUAL_NUMBER;
    }
}

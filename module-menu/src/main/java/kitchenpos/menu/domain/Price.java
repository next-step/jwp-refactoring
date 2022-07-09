package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;
import org.springframework.util.ObjectUtils;

@Embeddable
public class Price {
    private static final int COMPARE_EQUAL_NUMBER = 0;

    private BigDecimal price;

    protected Price() {

    }

    private Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (ObjectUtils.isEmpty(price) || isZeroOver(price)) {
            throw new IllegalArgumentException("가격은 0원 초과이어야 합니다.");
        }
    }

    private boolean isZeroOver(BigDecimal price) {
        return BigDecimal.ZERO.compareTo(price) >= COMPARE_EQUAL_NUMBER;
    }


    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    public static Price from(int price) {
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


    private boolean isBigThen(BigDecimal target) {
        return this.value().compareTo(target) > COMPARE_EQUAL_NUMBER;
    }

    public boolean isBigThen(Price target) {
        return isBigThen(target.value());
    }

    public boolean isBigThen(Amount target) {
        return isBigThen(target.value());
    }

    public BigDecimal multiply(Quantity quantity) {
        return price.multiply(BigDecimal.valueOf(quantity.value()));
    }
}

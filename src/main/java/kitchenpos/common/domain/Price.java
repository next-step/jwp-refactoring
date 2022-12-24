package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final BigDecimal MIN = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {}

    private Price(BigDecimal price) {
        validateNonNull(price);
        validateNotUnderPrice(price);
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    public static Price from(long price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price zero(){
        return new Price(BigDecimal.ZERO);
    }

    private static void validateNonNull(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("금액이 비었습니다.");
        }
    }

    private static void validateNotUnderPrice(BigDecimal price) {
        if (price.compareTo(MIN) < 0) {
            throw new IllegalArgumentException("금액은 " + MIN + "원 이상이어야 합니다.");
        }
    }

    public BigDecimal value() {
        return price;
    }

    public Price add(Price price) {
        return new Price(this.price.add(price.value()));
    }

    public Price multiply(Quantity quantity) {
        return new Price(this.price.multiply(BigDecimal.valueOf(quantity.value())));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}

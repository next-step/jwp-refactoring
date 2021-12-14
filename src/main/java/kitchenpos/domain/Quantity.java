package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private static final String INVALID_QUANTITY = "Quantity 는 0 이상의 숫자로 생성할 수 있습니다.";

    @Column(name = "quantity", nullable = false)
    private long quantity;

    protected Quantity() {}

    private Quantity(long quantity) {
        this.quantity = quantity;
    }

    public static Quantity from(long quantity) {
        validateQuantity(quantity);
        return new Quantity(quantity);
    }

    public long getValue() {
        return quantity;
    }

    private static void validateQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(INVALID_QUANTITY);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Quantity quantity1 = (Quantity)o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}

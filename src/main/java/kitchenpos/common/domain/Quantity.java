package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(columnDefinition = "BIGINT(20)", nullable = false)
    private long quantity;

    protected Quantity() {
    }

    private Quantity(long quantity) {
        validQuantity(quantity);
        this.quantity = quantity;
    }

    public static Quantity from(long quantity) {
        return new Quantity(quantity);
    }

    public long value() {
        return quantity;
    }

    private void validQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0개 이상이어야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Quantity quantity1 = (Quantity) o;

        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return (int) (quantity ^ (quantity >>> 32));
    }
}

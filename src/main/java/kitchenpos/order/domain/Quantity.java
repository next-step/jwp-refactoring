package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private static final long MIN_QUANTITY = 0;

    @Column(name = "quantity")
    private long quantity;

    protected Quantity() {
    }

    public Quantity(final long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException(String.format("상품 수는 최소 %d개 이상이어야 합니다.", MIN_QUANTITY));
        }
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}

package kitchenpos.common.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private static final String INVALID_QUANTITY_EXCEPTION = "개수 정보는 음수일 수 없습니다.";
    private long quantity;

    protected Quantity() {

    }

    public Quantity(long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(INVALID_QUANTITY_EXCEPTION);
        }
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}

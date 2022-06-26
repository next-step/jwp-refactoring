package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    @Column(nullable = false)
    private long quantity;

    protected Quantity() {
    }

    private Quantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    public static Quantity from(long quantity) {
        return new Quantity(quantity);
    }

    public long getValue() {
        return quantity;
    }

    private void validate(long quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("메뉴 상품의 수량은 1 이상이어야 합니다.");
        }
    }

}

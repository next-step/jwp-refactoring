package kitchenpos.domain;

import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {

    private long quantity;

    public MenuProductQuantity() {
    }

    public MenuProductQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}

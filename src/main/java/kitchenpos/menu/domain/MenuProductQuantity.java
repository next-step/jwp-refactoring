package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {
    @Column
    private long quantity;

    protected MenuProductQuantity() {
    }

    protected MenuProductQuantity(long quantity) {
        this.quantity = quantity;
    }

    public static MenuProductQuantity of(long quantity) {
        return new MenuProductQuantity(quantity);
    }

    public long getQuantity() {
        return quantity;
    }

}

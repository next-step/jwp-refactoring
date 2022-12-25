package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.menuconstants.MenuErrorMessages;

@Embeddable
public class MenuProductQuantity {

    private static final long MIN_QUANTITY = 1;
    @Column(name = "quantity", nullable = false)
    private long quantity;

    public MenuProductQuantity() {}

    public MenuProductQuantity(long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException(MenuErrorMessages.MENU_PRODUCT_QUANTITY_BELOW_ZERO);
        }
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProductQuantity that = (MenuProductQuantity) o;
        return quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}

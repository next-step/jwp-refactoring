package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.constants.ErrorMessages;

@Embeddable
public class MenuProductQuantity {

    @Column(name = "quantity", nullable = false)
    private long quantity;

    public MenuProductQuantity() {}

    public MenuProductQuantity(long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(ErrorMessages.MENU_PRODUCT_QUANTITY_BELOW_ZERO);
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

package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuProductQuantity {
    private static final int MIN_NUM = 0;

    @Column
    private long quantity;

    protected MenuProductQuantity() {}

    public MenuProductQuantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if(quantity < MIN_NUM) {
            throw new IllegalArgumentException(ErrorMessage.MENU_PRODUCT_INVALID_QUANTITY.getMessage());
        }
    }

    public long value() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductQuantity that = (MenuProductQuantity) o;
        return quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}

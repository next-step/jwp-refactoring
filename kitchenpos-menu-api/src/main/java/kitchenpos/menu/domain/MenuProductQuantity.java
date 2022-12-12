package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuProductExceptionCode;
import kitchenpos.utils.NumberUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {
    @Column
    private long quantity;

    protected MenuProductQuantity() {}

    public MenuProductQuantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if(NumberUtil.isNotPositiveNumber(quantity)) {
            throw new IllegalArgumentException(MenuProductExceptionCode.INVALID_QUANTITY.getMessage());
        }
    }

    public long getQuantity() {
        return quantity;
    }
}

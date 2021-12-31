package kitchenpos.domain.menu.domain;

import kitchenpos.domain.menu.exception.IllegalMenuProductException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class MenuProductQuantity {
    private static final String MIN_VALUE_ERROR_MESSAGE = "메뉴 상품 갯수는 최소 1개 이상 이어야 합니다.";
    private static final int MIN_VALUE = 1;
    @Column(nullable = false)
    private long quantity;

    protected MenuProductQuantity() {
    }

    protected MenuProductQuantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if (quantity < MIN_VALUE) {
            throw new IllegalMenuProductException(MIN_VALUE_ERROR_MESSAGE);
        }
    }

    public static MenuProductQuantity of(long quantity) {
        return new MenuProductQuantity(quantity);
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(quantity);
    }
}

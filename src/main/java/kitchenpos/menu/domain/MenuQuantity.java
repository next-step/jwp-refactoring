package kitchenpos.menu.domain;

import kitchenpos.menu.exception.IllegalMenuQuantityException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * packageName : kitchenpos.menu.domain
 * fileName : Quantity
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
@Embeddable
public class MenuQuantity {
    @Transient
    public static final Long MIN_QUANTITY = 0L;

    @Column(nullable = false)
    private Long quantity;

    protected MenuQuantity() {
    }

    public MenuQuantity(Long value) {
        validate(value);
        this.quantity = value;
    }

    private void validate(Long value) {
        if (Objects.isNull(value) || value < MIN_QUANTITY) {
            throw new IllegalMenuQuantityException();
        }
    }

    public static MenuQuantity of(Long value) {
        return new MenuQuantity(value);
    }

    public Long value() {
        return quantity;
    }
}

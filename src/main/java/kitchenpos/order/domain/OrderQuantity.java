package kitchenpos.order.domain;

import kitchenpos.order.exception.IllegalOrderQuantityException;

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
public class OrderQuantity {
    @Transient
    public static final Long MIN_QUANTITY = 0L;

    @Column(nullable = false)
    private Long quantity;

    protected OrderQuantity() {
    }

    public OrderQuantity(Long value) {
        validate(value);
        this.quantity = value;
    }

    private void validate(Long value) {
        if (Objects.isNull(value) || value < MIN_QUANTITY) {
            throw new IllegalOrderQuantityException();
        }
    }

    public static OrderQuantity of(Long value) {
        return new OrderQuantity(value);
    }

    public Long value() {
        return quantity;
    }
}

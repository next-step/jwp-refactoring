package kitchenpos.order.domain;

import kitchenpos.common.ErrorCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderLineItem {
    private static final long ZERO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private Long menuId;

    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Order order, Long menuId, long quantity) {
        validate(order, quantity);

        updateOrder(order);
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private void validate(Order order, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_ORDER.getErrorMessage());
        }
        if (isNotPositiveNumber(quantity)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_QUANTITY.getErrorMessage());
        }
    }

    void updateOrder(Order order) {
        if (this.order != order) {
            this.order = order;
            order.addOrderLineItem(this);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
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

        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(order, that.order) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, menuId);
    }

    private boolean isNotPositiveNumber(long quantity) {
        return quantity < ZERO;
    }
}

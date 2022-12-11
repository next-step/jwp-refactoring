package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderLineItemExceptionCode;
import kitchenpos.utils.NumberUtil;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private Long menuId;

    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Order order, Long menuId, long quantity) {
        validate(quantity);

        updateOrder(order);
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if (NumberUtil.isNotPositiveNumber(quantity)) {
            throw new IllegalArgumentException(OrderLineItemExceptionCode.INVALID_QUANTITY.getMessage());
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
}

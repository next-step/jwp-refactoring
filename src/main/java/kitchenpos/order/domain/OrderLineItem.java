package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Order order, Menu menu, long quantity) {
        validate(order, menu, quantity);

        updateOrder(order);
        this.menu = menu;
        this.quantity = quantity;
    }

    private void validate(Order order, Menu menu, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException(OrderLineItemExceptionCode.REQUIRED_ORDER.getMessage());
        }

        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(OrderLineItemExceptionCode.REQUIRED_MENU.getMessage());
        }

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

    public Menu getMenu() {
        return menu;
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
        return Objects.equals(order, that.order) && Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, menu);
    }
}

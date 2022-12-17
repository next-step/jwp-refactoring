package kitchenpos.order.domain;

import kitchenpos.exception.OrderLineItemErrorMessage;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    private static final int COMPARE_NUM = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Order order, Menu menu, long quantity) {
        validate(order, menu, quantity);
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    private void validate(Order order, Menu menu, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException(OrderLineItemErrorMessage.REQUIRED_ORDER.getMessage());
        }
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(OrderLineItemErrorMessage.REQUIRED_MENU.getMessage());
        }
        if (quantity < COMPARE_NUM) {
            throw new IllegalArgumentException(OrderLineItemErrorMessage.INVALID_QUANTITY.getMessage());
        }
    }

    public void updateOrder(Order order) {
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
}

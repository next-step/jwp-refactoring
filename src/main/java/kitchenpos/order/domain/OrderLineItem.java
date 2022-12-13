package kitchenpos.order.domain;

import kitchenpos.exception.OrderError;
import kitchenpos.exception.OrderLineItemError;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "menu_id")),
            @AttributeOverride(name = "name", column = @Column(name = "menu_name")),
            @AttributeOverride(name = "price.price", column = @Column(name = "menu_price"))
    })
    private OrderMenu menu;

    private long quantity;

    protected OrderLineItem() {

    }

    public OrderLineItem(Order order, OrderMenu menu, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException(OrderLineItemError.REQUIRED_ORDER);
        }
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(OrderLineItemError.REQUIRED_MENU);
        }
        if (quantity < 0) {
            throw new IllegalArgumentException(OrderLineItemError.INVALID_QUANTITY);
        }

        updateOrder(order);
        this.menu = menu;
        this.quantity = quantity;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public OrderMenu getMenu() {
        return menu;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity;
    }
}

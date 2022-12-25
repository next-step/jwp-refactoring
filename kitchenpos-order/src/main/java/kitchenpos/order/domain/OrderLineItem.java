package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderLineItemQuantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;
    private Long menuId;

    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Order order, Long menuId, long quantity) {
        updateOrder(order);
        this.menuId = menuId;
        this.quantity = new OrderLineItemQuantity(quantity);
    }

    public void updateOrder(Order order) {
        if (this.order != order) {
            this.order = order;
            order.addOrderLineItem(this);
        }
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity.value();
    }
}

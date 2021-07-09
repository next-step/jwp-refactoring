package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private OrderMenu orderMenu;

    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(OrderMenu orderMenu, long quantity) {
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return this.order.getId();
    }

    public Long getMenuId() {
        return orderMenu.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    void toOrder(Order order) {
        this.order = order;
    }
}

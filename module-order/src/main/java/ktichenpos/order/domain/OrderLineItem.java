package ktichenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Order order;
    @Embedded
    private OrderLineMenu orderLineMenu;

    private long quantity;

    protected OrderLineItem() {

    }

    public OrderLineItem(OrderLineMenu orderLineMenu, long quantity) {
        this.orderLineMenu = orderLineMenu;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public OrderLineMenu getOrderLineMenu() {
        return orderLineMenu;
    }

    public long getQuantity() {
        return quantity;
    }

    void includeToOrder(Order order) {
        this.order = order;
    }
}

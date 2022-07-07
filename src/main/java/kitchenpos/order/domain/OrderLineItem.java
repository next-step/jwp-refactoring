package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Embedded
    private OrderMenu orderMenu;
    @Embedded
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(OrderMenu orderMenu, long quantity) {
        this.orderMenu = orderMenu;
        this.quantity = new Quantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}

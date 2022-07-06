package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @Embedded
    private OrderMenu orderMenu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(OrderMenu orderMenu, long quantity) {
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

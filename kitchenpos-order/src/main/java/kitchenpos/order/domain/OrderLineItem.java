package kitchenpos.order.domain;

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
    private OrderMenu orderMenu;

    private long quantity;

    protected OrderLineItem() {

    }

    public OrderLineItem(Long seq, Order order, OrderMenu orderMenu, long quantity) {
        this(order, orderMenu, quantity);
        this.seq = seq;
    }

    public OrderLineItem(Order order, OrderMenu orderMenu, long quantity) {
        this(orderMenu, quantity);
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException();
        }
        updateOrder(order);
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public OrderLineItem(OrderMenu orderMenu, long quantity) {
        if (Objects.isNull(orderMenu)) {
            throw new IllegalArgumentException();
        }
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }

        this.orderMenu = orderMenu;
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

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}

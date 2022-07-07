package kitchenpos.order.domain;

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
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Order orderId, final Menu menu, final long quantity) {
        this.seq = seq;
        this.order = orderId;
        this.menu = menu;
        this.quantity = quantity;
    }

    private OrderLineItem(final Menu menu, final long quantity) {
        this(null, null, menu, quantity);
    }

    public static OrderLineItem of(final Menu menu, final long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public void updateOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public String getMenuName() {
        return menu.getName();
    }

    public long getMenuPrice() {
        return menu.getPrice().longValue();
    }

    public Order getOrder() {
        return order;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "seq=" + seq +
                ", orderId=" + order.getId() +
                ", menu=" + menu +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(order, that.order) && Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, menu, quantity);
    }
}


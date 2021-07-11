package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Order order, final Menu menu, final long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void updateOrder(final Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final OrderLineItem that = (OrderLineItem)o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(order,
            that.order) && Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, menu, quantity);
    }
}

package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "order_id")
    private Long orderId;

    @JoinColumn(name = "menu_id")
    private Long menuId;

    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Order order, final Menu menu, final long quantity) {
        this.orderId = order.getId();
        this.menuId = menu.getId();
        this.quantity = new Quantity(quantity);
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this.menuId = menu.getId();
        this.quantity = new Quantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity.value();
    }

    public void updateOrder(final Order order) {
        this.orderId = order.getId();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final OrderLineItem that = (OrderLineItem)o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(orderId,
            that.orderId) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, orderId, menuId, quantity);
    }
}

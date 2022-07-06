package kitchenpos.order.domain;

import java.util.Objects;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private long menuId;
    private long quantity;

    public OrderLineItem() {
    }


    public OrderLineItem(long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public static OrderLineItem of(long menuId, int quantity) {
        return new OrderLineItem(menuId, quantity);
    }


    public void toOrder(final Order order) {
        if (this.order != null) {
            this.order.getOrderLineItems().remove(this);
        }
        this.order = order;
    }


    public Order getOrder() {
        return order;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(order, that.order) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, menuId, quantity);
    }
}

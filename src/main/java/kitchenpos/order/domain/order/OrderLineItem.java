package kitchenpos.order.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "order_id", nullable = false, insertable = false, updatable = false)
    private Long orderId;

    @Column(name = "menu_id", nullable = false, insertable = false, updatable = false)
    private Long menuId;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    protected void updateOrder(final Long orderId) {
        this.orderId = orderId;
    }
}

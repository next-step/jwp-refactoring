package kitchenpos.order.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menu.domain.menu.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "order_id", nullable = false, insertable = false, updatable = false)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this.menu = menu;
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

    public Menu getMenu() {
        return this.menu;
    }

    protected void updateOrder(final Long orderId) {
        this.orderId = orderId;
    }
}

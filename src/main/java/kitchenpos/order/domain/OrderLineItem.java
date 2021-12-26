package kitchenpos.order.domain;

import kitchenpos.domain.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private Quantity quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = Quantity.of(quantity);
    }

    public static OrderLineItem of(Long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public static OrderLineItem of(Menu menu, long quantity) {
        return new OrderLineItem(null, null, menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public void setOrderId(final Long orderId) {
        this.order.setId(orderId);
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public void setMenuId(final Long menuId) {
        this.menu.setId(menuId);
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }

    public void setQuantity(final long quantity) {
        this.quantity = Quantity.of(quantity);
    }
}

package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    protected OrderLineItem() {}

    public OrderLineItem(Long seq, long quantity, Menu menu) {
        this.seq = seq;
        this.quantity = quantity;
        this.menu = menu;
    }

    public OrderLineItem(long quantity, Menu menu) {
        this.quantity = quantity;
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenuId(final Menu menu) {
        this.menu = menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}

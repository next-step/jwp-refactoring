package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Embedded
    private Quantity quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    protected OrderLineItem() {}

    public OrderLineItem(Quantity quantity, Menu menu) {
        this.quantity = quantity;
        this.menu = menu;
    }

    public OrderLineItem(Long seq, Quantity quantity, Menu menu) {
        this(quantity, menu);
        this.seq = seq;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}

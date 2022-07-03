package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Long seq, Order order, Menu menu, Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(Menu menu, Quantity quantity) {
        this(null, null, menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void associateOrder(Order order) {
        this.order = order;
    }
}

package kitchenpos.order.domain;

import common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Quantity quantity;

    protected OrderLineItem() {

    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = new Quantity(quantity);
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

    public long getQuantity() {
        return quantity.getQuantity();
    }
}

package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    public long getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this(null, order, menu, quantity);
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem createOrderLineItem(Order order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}

package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private int quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Menu menu, int quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    private OrderLineItem(Menu menu, int quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Menu menu, int quantity) {
        return new OrderLineItem(seq, menu, quantity);
    }

    public static OrderLineItem of(Menu menu, int quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public void registerOrder(Order order) {
        this.order = order;
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

    public int getQuantity() {
        return quantity;
    }
}

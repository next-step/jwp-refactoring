package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @ManyToOne
    private Menu menu;
    private long quantity;

    public OrderLineItem(Menu menu, Long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem() {

    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void makeOrder(Order order) {
        this.order = order;
        order.addOrderLineItem(this);
    }

    public long getQuantity() {
        return quantity;
    }
}

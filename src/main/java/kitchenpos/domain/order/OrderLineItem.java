package kitchenpos.domain.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.domain.menu.Menu;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {

    }

    private OrderLineItem(Orders order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Orders order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }

    public long getSeq() {
        return this.seq;
    }

    public Orders getOrder() {
        return this.order;
    }
    public Menu getMenu() {
        return this.menu;
    }

    public long getQuantity() {
        return this.quantity;
    }

    public void changeOrder(Orders order) {
        this.order = order;
    }
}

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
    private Long seq;

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

    public static OrderLineItem of(Menu menu, long quantity) {
        return new OrderLineItem(null, menu, quantity);
    }

    public Long getSeq() {
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

    public void acceptOrder(Orders order) {
        if (this.order != null) {
            this.order.getOrderLineItems().remove(this);
        }

        this.order = order;
        this.order.getOrderLineItems().add(this);
    }
}

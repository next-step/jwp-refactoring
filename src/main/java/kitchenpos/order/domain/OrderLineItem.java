package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = new Quantity(quantity);
    }

    public void mappingOrder(Order order) {
        this.order = order;
    }

    public Long id() {
        return id;
    }

    public Order order() {
        return order;
    }

    public Menu menu() {
        return menu;
    }

    public long quantity() {
        return quantity.quantity();
    }
}

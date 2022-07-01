package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long seq, Order order, Menu menu, Quantity quantity) {
        this(menu, quantity);
        this.seq = seq;
        this.order = order;
    }

    private OrderLineItem(Menu menu, Quantity quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Menu menu, Quantity quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public static OrderLineItem of(Long seq, Order order, Menu menu, Quantity quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
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

    public long getQuantity() {
        return quantity.getQuantity();
    }
}

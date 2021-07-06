package kitchenpos.order.domain;

import kitchenpos.common.domian.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Quantities;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    public OrderLineItem() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    private OrderLineItem(Order order, Menu menu, Quantity quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Order order, Menu menu, Quantity quantity) {
        OrderLineItem orderLineItem = new OrderLineItem(order, menu, quantity);
        order.addOrderLineItem(orderLineItem);
        return new OrderLineItem(order, menu, quantity);
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
}

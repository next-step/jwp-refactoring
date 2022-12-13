package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order2 order;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id")
    private Menu2 menu;

    private long quantity;

    protected OrderLineItem2() {
    }

    public OrderLineItem2(Order2 order, Menu2 menu, long quantity) {
        setOrder(order);
        this.menu = menu;
        this.quantity = quantity;
    }

    public void setOrder(Order2 newOrder) {
        if (Objects.nonNull(order)) {
            order.getOrderLineItems().remove(this);
        }

        order = newOrder;

        if (!order.getOrderLineItems().contains(this)) {
            order.getOrderLineItems().add(this);
        }
    }
}

package kitchenpos.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    private Order order;

    @ManyToOne(fetch = LAZY)
    private Menu menu;

    private Long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Menu menu, Long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.getOrderLineItems().remove(this);
        }
        this.order = order;
        order.getOrderLineItems().add(this);
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

    public Long getQuantity() {
        return quantity;
    }
}

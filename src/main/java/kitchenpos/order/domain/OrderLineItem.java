package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Embedded
    private OrderQuantity orderQuantity;

    protected OrderLineItem() {

    }

    private OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.orderQuantity = new OrderQuantity(quantity);
    }

    public static OrderLineItem create(Order order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return orderQuantity.getQuantity();
    }

}

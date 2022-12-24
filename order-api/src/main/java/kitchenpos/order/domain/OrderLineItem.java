package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "menuId", column = @Column(name = "menu_id")),
            @AttributeOverride(name = "name.name", column = @Column(name = "menu_name")),
            @AttributeOverride(name = "price.price", column = @Column(name = "menu_price"))
    })
    private OrderMenu orderMenu;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(OrderMenu orderMenu, long quantity) {
        this.orderMenu = orderMenu;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(OrderMenu orderMenu, long quantity) {
        return new OrderLineItem(orderMenu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public long getQuantity() {
        return quantity.value();
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }
}

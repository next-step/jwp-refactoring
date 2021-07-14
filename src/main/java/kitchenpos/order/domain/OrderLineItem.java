package kitchenpos.order.domain;

import kitchenpos.common.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long menuId;

    private Quantity quantity;

    public OrderLineItem() {}

    public OrderLineItem(Long id, Order order, Long menuId, long quantity) {
        this.id = id;
        this.order = order;
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
    }

    public OrderLineItem(Long id, Long menuId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
    }

    public OrderLineItem(Order order, Long menuId, Quantity quantity) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Order order, Long menuId, Quantity quantity) {
        return new OrderLineItem(order, menuId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity.quantity();
    }
}

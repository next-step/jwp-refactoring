package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private Long menuId;
    private long quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(Long id, Order order, Long menuId, Long quantity) {
        this.id = id;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long id, Order order, Long menuId, Long quantity) {
        return new OrderLineItem(id, order, menuId, quantity);
    }

    public void changeOrderLineItem(Order order) {
        this.order = order;
        order.addOrderLineItem(this);
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

    public long getQuantity() {
        return quantity;
    }
}

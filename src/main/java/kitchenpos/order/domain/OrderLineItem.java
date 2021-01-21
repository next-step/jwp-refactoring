package kitchenpos.order.domain;

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

    @Column(name = "menu_id")
    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = Quantity.of(quantity);
    }

    public void updateOrder(Order order) {
        this.order = order;
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
        return quantity.getQuantity();
    }
}

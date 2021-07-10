package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;

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

    private Long menuId;

    @Embedded
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
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

    public Long menuId() {
        return menuId;
    }

    public long quantity() {
        return quantity.quantity();
    }
}

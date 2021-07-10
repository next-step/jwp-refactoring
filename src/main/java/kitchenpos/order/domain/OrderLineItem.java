package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    private Long menuId;

    @Embedded
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
    }

    public void mappingOrder(Long orderId) {
        this.orderId = orderId;
    }

    public Long id() {
        return id;
    }

    public Long orderId() {
        return orderId;
    }

    public Long menuId() {
        return menuId;
    }

    public long quantity() {
        return quantity.quantity();
    }
}

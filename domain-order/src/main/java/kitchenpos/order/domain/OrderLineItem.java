package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.domain.vo.Quantity;

@Entity
public class OrderLineItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private OrderMenu orderMenu;

    @AttributeOverride(name = "value", column = @Column(name = "quantity", nullable = false))
    private Quantity quantity;

    protected OrderLineItem() {}

    public OrderLineItem(OrderMenu orderMenu, long quantity) {
        this(orderMenu, Quantity.of(quantity));
    }

    public OrderLineItem(OrderMenu orderMenu, Quantity quantity) {
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return this.order.getId();
    }

    public Long getMenuId() {
        return orderMenu.getId();
    }

    public Quantity getQuantity() {
        return quantity;
    }

    void toOrder(Order order) {
        this.order = order;
    }
}

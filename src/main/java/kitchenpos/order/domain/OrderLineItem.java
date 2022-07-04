package kitchenpos.order.domain;

import kitchenpos.common.Quantity;
import kitchenpos.order.dto.OrderLineItemRequest;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Long menuId;
    @Embedded
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Order order, Long menuId, long quantity) {
        this(null, order, menuId, new Quantity(quantity));
    }

    public OrderLineItem(Long seq, Order order, Long menuId, Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(OrderLineItemRequest request) {
        return of(request, null);
    }

    public static OrderLineItem of(OrderLineItemRequest request, Order order) {
        return new OrderLineItem(order, request.getMenuId(), request.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}

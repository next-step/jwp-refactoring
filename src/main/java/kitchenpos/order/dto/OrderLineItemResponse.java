package kitchenpos.order.dto;

import java.util.Objects;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final Long quantity;

    private OrderLineItemResponse(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.seq(), orderLineItem.order().id(), orderLineItem.menu().id(),
                orderLineItem.quantity().value());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItemResponse that = (OrderLineItemResponse) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}

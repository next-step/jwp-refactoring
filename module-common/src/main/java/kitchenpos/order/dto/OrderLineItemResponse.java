package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.Objects;

public class OrderLineItemResponse {
    private Long seq;
    private Long MenuId;
    private Long quantity;

    protected OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, Long menuId, Long quantity) {
        this.seq = seq;
        MenuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItemResponse that = (OrderLineItemResponse) o;
        return Objects.equals(seq, that.seq) && Objects.equals(MenuId, that.MenuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, MenuId, quantity);
    }
}

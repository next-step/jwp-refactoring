package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderLineItemResponse {

    private Long id;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, Long menuId, long quantity) {
        this.id = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> fromList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());
    }


    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantityVal());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
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
        return getQuantity() == that.getQuantity() && Objects.equals(getId(), that.getId())
            && Objects.equals(getMenuId(), that.getMenuId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMenuId(), getQuantity());
    }
}

package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.MenuId;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineResponse {

    private Long id;
    private Long orderId;
    private MenuId menuId;
    private long quantity;

    public static OrderLineResponse of(OrderLineItem orderLineItem) {
        return new OrderLineResponse(
            orderLineItem.getId(),
            orderLineItem.getOrder().getId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity());
    }

    public static List<OrderLineResponse> ofList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineResponse::of)
            .collect(Collectors.toList());
    }

    public OrderLineResponse(Long id, Long orderId, MenuId menuId, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public MenuId getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}

package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class OrderLineItemRequest {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    private static Long setOrderLineItemOrderId(OrderLineItem orderLineItem) {
        Long orderId = null;
        Order order = orderLineItem.getOrder();
        if (order != null) {
            orderId = order.getId();
        }
        return orderId;
    }

    private static Long setOrderLineItemMenuId(OrderLineItem orderLineItem) {
        Long menuId = null;
        Menu menu = orderLineItem.getMenu();
        if (menu != null) {
            menuId = menu.getId();
        }
        return menuId;
    }

    public static OrderLineItemRequest of(OrderLineItem orderLineItem) {
        Long orderId = setOrderLineItemOrderId(orderLineItem);
        Long menuId = setOrderLineItemMenuId(orderLineItem);

        return OrderLineItemRequest.builder()
                .seq(orderLineItem.getSeq())
                .orderId(orderId)
                .menuId(menuId)
                .quantity(orderLineItem.getQuantity())
                .build();
    }
}

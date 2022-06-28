package kitchenpos.dto.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemRequest {

    private final Long id;
    private final Long orderId;
    private final Long menuId;
    private final Long quantity;

    public OrderLineItemRequest(Long id, Long orderId, Long menuId, Long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemRequest> of(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(orderLineItem ->
                new OrderLineItemRequest(orderLineItem.getId(), null,
                    orderLineItem.getOrderMenu().getMenuId(), orderLineItem.getQuantity()))
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
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

}

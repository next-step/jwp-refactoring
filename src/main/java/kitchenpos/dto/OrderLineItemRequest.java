package kitchenpos.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;

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

    public static List<OrderLineItemRequest> of(OrderLineItem... orderLineItems) {
        return Arrays.stream(orderLineItems)
            .map(orderLineItem ->
                new OrderLineItemRequest(orderLineItem.getId(), null,
                    orderLineItem.getMenuId(), orderLineItem.getQuantity()))
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

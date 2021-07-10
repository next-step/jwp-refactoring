package kitchenpos.ui.dto.order;

import kitchenpos.domain.order.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemRequest {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    protected OrderLineItemRequest() {
    }

    private OrderLineItemRequest(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, long quantity) {
        return new OrderLineItemRequest(null, null, menuId, quantity);
    }

    public static OrderLineItemRequest of(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getSeq(), orderLineItem.getOrder().getId(), orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    public static List<OrderLineItemRequest> ofList(List<OrderLineItem> menuProducts) {
        return menuProducts.stream()
                .map(OrderLineItemRequest::of)
                .collect(Collectors.toList());
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

    public long getQuantity() {
        return quantity;
    }
}

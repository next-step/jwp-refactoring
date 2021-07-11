package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.ArrayList;
import java.util.List;

public class OrderLineItemResponse {
    private Long id;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long id, Long orderId, Long menuId, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItems) {
        List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();

        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItemResponses.add(new OrderLineItemResponse(orderLineItem.id(), orderLineItem.orderId(), orderLineItem.menuId(), orderLineItem.quantity()));
        }

        return orderLineItemResponses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}

package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.util.List;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderSaveRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class OrderSaveRequest {
    private Long orderTableId;
    private List<OrderLineItemSaveRequest> orderLineItems;

    private OrderSaveRequest() {
    }

    private OrderSaveRequest(Long orderTableId, List<OrderLineItemSaveRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderSaveRequest of(Long orderTableId, List<OrderLineItemSaveRequest> orderLineItems) {
        return new OrderSaveRequest(orderTableId, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemSaveRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity() {
        return new Order(orderTableId, OrderLineItemSaveRequest.toEntities(orderLineItems));
    }
}

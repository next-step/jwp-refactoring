package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderStatusUpdateRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class OrderStatusUpdateRequest {
    private OrderStatus orderStatus;

    private OrderStatusUpdateRequest() {
    }

    private OrderStatusUpdateRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusUpdateRequest of(OrderStatus orderstatus) {
        return new OrderStatusUpdateRequest(orderstatus);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}

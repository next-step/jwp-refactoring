package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderStatusUpdateRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class OrderStatusUpdateRequest {
    private OrderStatus orderStatus;

    public OrderStatusUpdateRequest() {
    }

    public OrderStatusUpdateRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}

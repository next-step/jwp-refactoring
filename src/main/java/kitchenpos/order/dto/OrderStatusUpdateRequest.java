package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderStatusUpdateRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
//FIXME 생성자 제한하기
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

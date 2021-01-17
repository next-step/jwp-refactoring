package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-16
 */
public class OrderStatusChangeDto {
    private OrderStatus orderStatus;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    protected OrderStatusChangeDto() {
    }

    public OrderStatusChangeDto(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

}

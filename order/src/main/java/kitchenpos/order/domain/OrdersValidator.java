package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderDto;

public interface OrdersValidator {
    public Orders getValidatedOrdersForCreate(OrderDto orderDto);

    public Orders getValidatedOrdersForChangeOrderStatus(Long orderId);
}

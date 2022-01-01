package kitchenpos.order.application;

import kitchenpos.common.exception.EmptyOrderTableException;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.common.exception.OrderStatusCompletedException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderValidator {
    private final OrderTableService orderTableService;

    public OrderValidator(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    public OrderTable validateCreateOrder(OrderRequest orderRequest) {
        Long orderTableId = orderRequest.getOrderTableId();
        orderTableId = Optional.ofNullable(orderTableId)
                .orElseThrow(NotFoundEntityException::new);

        OrderTable orderTable = orderTableService.findOrderTableById(orderTableId);

        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException();
        }

        return orderTable;
    }

    public void validateChangeOrderStatus(Order order) {
        if (OrderStatus.isCompleted(order.getOrderStatus())) {
            throw new OrderStatusCompletedException();
        }
    }
}

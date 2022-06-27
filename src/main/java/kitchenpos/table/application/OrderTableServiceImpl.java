package kitchenpos.table.application;

import java.util.Arrays;
import kitchenpos.core.exception.CannotUpdateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableServiceImpl implements OrderTableService {

    private final OrderRepository orderRepository;

    public OrderTableServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderTableStatus(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new CannotUpdateException(ExceptionType.CAN_NOT_UPDATE_TABLE_IN_COOKING_AND_MEAL_STATUS);
        }
    }
}

package kitchenpos.order.application;

import java.util.Arrays;
import kitchenpos.common.exception.CannotUpdateException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.OrderTableStatusService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableStatusServiceImpl implements OrderTableStatusService {

    private final OrderRepository orderRepository;

    public OrderTableStatusServiceImpl(OrderRepository orderRepository) {
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

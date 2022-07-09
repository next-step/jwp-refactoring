package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableStatusService;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderTableStatusService implements TableStatusService {
    private final OrderRepository orderRepository;

    public OrderTableStatusService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderTableStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new BadRequestException(ErrorCode.CAN_NOT_CHANGE_COOKING_AND_MEAL);
        }
    }

    @Override
    public void validateOrderTableStatus(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new BadRequestException(ErrorCode.CAN_NOT_CHANGE_COOKING_AND_MEAL);
        }
    }
}

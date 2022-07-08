package kitchenpos.order.application;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableStatusService;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
}

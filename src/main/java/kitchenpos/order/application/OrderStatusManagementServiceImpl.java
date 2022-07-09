package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.exception.InvalidValueException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.OrderStatusManagementService;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusManagementServiceImpl implements OrderStatusManagementService {
    private final OrderRepository orderRepository;

    public OrderStatusManagementServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusBeChanged(Long orderTableId) {
        if(orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidValueException();
        }
    }

    @Override
    public void validateOrderStatusBeChangedByIds(List<Long> orderTableIds) {
        if(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidValueException();
        }
    }
}

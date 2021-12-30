package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderValidator;
import kitchenpos.table.exception.CannotChangeEmptyException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderValidatorImpl implements OrderValidator {
    OrderRepository orderRepository;

    public void canUngroupOrChange(Long id) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(id, Arrays.asList(OrderStatus.COOKING.name(),
                OrderStatus.MEAL.name()))) {
            throw new CannotChangeEmptyException();
        }
    }

    public void canUngroupOrChangeOrderList(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING.name(),
                OrderStatus.MEAL.name()))) {
            throw new CannotChangeEmptyException();
        }
    }
}

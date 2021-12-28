package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.CannotChangeEmptyException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class OrderValidator {
    OrderRepository orderRepository;
    public void canUngroupOrChange(Long id) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(id, Arrays.asList(OrderStatus.COOKING.name(),
                OrderStatus.MEAL.name()))) {
            throw new CannotChangeEmptyException();
        }
    }
}

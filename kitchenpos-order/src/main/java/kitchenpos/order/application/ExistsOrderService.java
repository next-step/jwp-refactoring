package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.ExistsOrderPort;
import org.springframework.stereotype.Service;

@Service
public class ExistsOrderService implements ExistsOrderPort {

    private final OrderRepository orderRepository;

    public ExistsOrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean existsOrderStatusCookingOrMeal(Long orderTableId) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    @Override
    public boolean existsOrderStatusCookingOrMeal(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}

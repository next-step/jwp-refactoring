package kitchenpos.order.domain;

import java.util.Collections;

import org.springframework.stereotype.Component;

import kitchenpos.ordertable.domain.OrderStatusCheckService;

@Component
public class OrderStatusCheckServiceImpl implements OrderStatusCheckService {
    private final OrderRepository orderRepository;

    public OrderStatusCheckServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean existsOrdersInProgress(Long orderTableId) {
        return orderRepository.existsAllByOrderTableIdInAndOrderStatusIn(
            Collections.singletonList(orderTableId), OrderStatus.getInProgressStatuses());
    }
}

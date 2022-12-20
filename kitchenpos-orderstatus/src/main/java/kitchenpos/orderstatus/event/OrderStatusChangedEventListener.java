package kitchenpos.orderstatus.event;

import kitchenpos.orderstatus.domain.OrderStatus;
import kitchenpos.orderstatus.repository.OrderStatusRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@EnableAsync
@Component
public class OrderStatusChangedEventListener implements ApplicationListener<OrderStatusChangedEvent> {

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusChangedEventListener(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    @Transactional
    @Async
    @Override
    public void onApplicationEvent(OrderStatusChangedEvent event) {
        final OrderStatus savedOrderStatus = orderStatusRepository.findByOrderId(event.getOrderId());
        savedOrderStatus.updateStatus(event.getChangedStatus());
        orderStatusRepository.save(savedOrderStatus);
    }
}

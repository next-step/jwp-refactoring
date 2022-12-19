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
public class OrderStatusCreatedEventListener implements ApplicationListener<OrderStatusCreatedEvent> {

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusCreatedEventListener(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    @Transactional
    @Async
    @Override
    public void onApplicationEvent(OrderStatusCreatedEvent event) {
        orderStatusRepository.save(
                new OrderStatus(event.getOrderId(), event.getOrderTableId(), event.getOrderStatus()));
    }
}

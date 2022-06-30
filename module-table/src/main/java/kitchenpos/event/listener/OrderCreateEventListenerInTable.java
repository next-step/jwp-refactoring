package kitchenpos.event.listener;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.event.OrderCreatedEvent;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateEventListenerInTable implements ApplicationListener<OrderCreateEvent> {

    private final OrderTableRepository orderTableRepository;

    public OrderCreateEventListenerInTable(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        OrderCreatedEvent orderCreatedEvent = (OrderCreatedEvent) event.getSource();

        validateCanAcceptOrder(orderCreatedEvent.getOrderTableId());

    }

    private void validateCanAcceptOrder(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderException("주문이 실행될 테이블은 저장이 되어있어야합니다"));

        if (orderTable.isEmpty()) {
            throw new OrderException("주문이 실행될 테이블은 비어있으면 안됩니다");
        }
    }
}

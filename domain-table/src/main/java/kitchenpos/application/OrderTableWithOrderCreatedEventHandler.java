package kitchenpos.application;

import kitchenpos.domain.OrderCreatedEvent;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableWithOrderCreatedEventHandler {
    private final OrderTableRepository orderTableRepository;

    public OrderTableWithOrderCreatedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        final OrderTable orderTable = orderTableRepository.findById(orderCreatedEvent.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 빈 테이블인 경우 주문할 수 없습니다.");
        }
    }
}

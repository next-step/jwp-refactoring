package kitchenpos.domain.orders.order.application;

import kitchenpos.domain.orders.orderTable.domain.OrderTable;
import kitchenpos.domain.orders.orderTable.domain.OrderTableRepository;
import kitchenpos.domain.orders.orderTable.event.OrderTableCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableEventHandler {
    private final OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional(readOnly = true)
    public void handle(OrderTableCreatedEvent event) {
        OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에서는 주문을 할수가 없습니다.");
        }
    }
}

package kitchenpos.order.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.event.OrderTableCreatedEvent;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableCreatedEventHandler {
    private final OrderTableRepository orderTableRepository;

    public OrderTableCreatedEventHandler(final OrderTableRepository orderTableRepository) {
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

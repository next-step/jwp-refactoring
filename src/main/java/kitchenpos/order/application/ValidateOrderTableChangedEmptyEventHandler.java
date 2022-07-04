package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;

import kitchenpos.Exception.UnCompletedOrderStatusException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableChangedEmptyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ValidateOrderTableChangedEmptyEventHandler {
    private final OrderRepository orderRepository;

    public ValidateOrderTableChangedEmptyEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(OrderTableChangedEmptyEvent event) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(event.getOrderTableId(),
                getCannotUngroupTableGroupStatus())) {
            throw new UnCompletedOrderStatusException("계산 완료 상태의 주문이 있는 테이블은 빈 테이블로 변경할 수 없습니다.");
        }
    }
}

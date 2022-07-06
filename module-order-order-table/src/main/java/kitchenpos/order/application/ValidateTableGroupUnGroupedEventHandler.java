package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;

import kitchenpos.exception.UnCompletedOrderStatusException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.TableGroupUnGroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ValidateTableGroupUnGroupedEventHandler {
    private final OrderRepository orderRepository;

    public ValidateTableGroupUnGroupedEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener(TableGroupUnGroupedEvent.class)
    @Transactional
    public void handle(TableGroupUnGroupedEvent event) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(event.getOrderTableIds(),
                getCannotUngroupTableGroupStatus())) {
            throw new UnCompletedOrderStatusException("단체 내 모든 테이블의 주문 상태가 주문 또는 식사 상태이면 단체 지정을 해제할 수 없습니다.");
        }
    }

}

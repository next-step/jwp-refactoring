package kitchenpos.table.application;

import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;

import kitchenpos.Exception.UnCompletedOrderStatusException;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, getCannotUngroupTableGroupStatus())) {
            throw new UnCompletedOrderStatusException("계산 완료 상태의 주문이 있는 테이블은 빈 테이블로 변경할 수 없습니다.");
        }
    }
}

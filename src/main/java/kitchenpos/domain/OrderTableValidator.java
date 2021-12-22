package kitchenpos.domain;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateToChangeEmpty(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정이 된 주문 테이블의 상태는 변경할 수 없습니다");
        }
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), OrderStatus.getNotCompletions())) {
            throw new IllegalArgumentException("주문 상태가 '조리' 혹은 '식사' 중일 경우, 테이블의 상태를 변경할 수 없습니다");
        }
    }
}

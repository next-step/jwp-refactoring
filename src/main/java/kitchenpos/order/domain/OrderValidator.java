package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateEmptyTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있는 상태에서는 주문을 생성할 수 없습니다");
        }
    }
}

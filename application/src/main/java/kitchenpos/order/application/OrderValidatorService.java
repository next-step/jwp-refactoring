package kitchenpos.order.application;

import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorService implements OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidatorService(OrderTableRepository orderTableRepository) {
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

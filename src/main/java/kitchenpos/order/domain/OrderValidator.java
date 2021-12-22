package kitchenpos.order.domain;

import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateOrder(Long orderTableId) {
        OrderTable orderTable = findOrderTable(orderTableId);
        validateOrderTable(orderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException("해당하는 테이블이 없습니다."));
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidArgumentException("빈 테이블은 주문을 할 수 없습니다.");
        }
    }
}

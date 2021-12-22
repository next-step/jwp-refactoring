package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.exception.CannotUpdatedException;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private static final Integer MIN_SIZE = 1;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        OrderTable orderTable = findOrderTable(orderTableId);
        validateOrderTable(orderTable);
        validateAddOrderLineItem(orderLineItems);
    }

    public void validateUpdateOrderStatus(Order order) {
        if (order.isCompletion()) {
            throw new CannotUpdatedException("계산완료된 주문은 변경할 수 없습니다.");
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidArgumentException("빈 테이블은 주문을 할 수 없습니다.");
        }
    }

    private void validateAddOrderLineItem(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() < MIN_SIZE) {
            throw new InvalidArgumentException("메뉴는 하나 이상 선택해야 합니다.");
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException("해당하는 테이블이 없습니다."));
    }

}
